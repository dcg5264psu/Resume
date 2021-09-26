////////////////////////////////////////////////////////////////////////////////
//
//  File          : lcloud_client.c
//  Description   : This is the client side of the Lion Clound network
//                  communication protocol.
//
//  Author        : Patrick McDaniel
//  Last Modified : Sat 28 Mar 2020 09:43:05 AM EDT
//

// Include Files
#include <signal.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <assert.h>
#include <stdint.h>

// Project Include Files
#include <lcloud_network.h>
#include <cmpsc311_log.h>
#include <cmpsc311_util.h>
//
//global variables
int socket_handle = -1;


// Functions


////////////////////////////////////////////////////////////////////////////////
//
// Function     : extract_lcoud_registers
// Description  : Unpack the register that is sent back from the bus.
//
// Inputs       : lc_res - the packed register from the bus
//                buf - a buffer to read out the different values of the register.
// Outputs      : integer representing pass or fail



int extract_lcloud_register(LCloudRegisterFrame lc_response,LCloudRegisterFrame buf[]) {
	

	 
	buf[0] = (lc_response & 0xf000000000000000); 		//Get Value of b0 and store in buffer
	buf[0] = buf[0]>> 60;								//Shift to the right to allow for the codes to allign with control codes.
	
	buf[1] = (lc_response & 0x0f00000000000000); 		//Get Value of b1 and store in buffer
	buf[1] = buf[1]>> 56;								//Shift to the right to allow for the codes to allign with control codes.

	buf[2] = (lc_response & 0x00ff000000000000); 		//Get Value of c0 and store in buffer
	buf[2] = buf[2]>> 48;								//Shift to the right to allow for the codes to allign with control codes.

	buf[3] = (lc_response & 0x0000ff0000000000); 		//Get Value of c1 and store in buffer
	buf[3] = buf[3]>> 40;								//Shift to the right to allow for the codes to allign with control codes.
	
	buf[4] = (lc_response & 0x000000ff00000000);		//Get Value of c2 and store in buffer
	buf[4] = buf[4]>> 32;								//Shift to the right to allow for the codes to allign with control codes.

	buf[5] = (lc_response & 0x00000000ffff0000); 		//Get Value of d0 and store in buffer
	buf[5] = buf[5]>> 16;								//Shift to the right to allow for the codes to allign with control codes.

	buf[6] = (lc_response & 0x000000000000ffff); 		//Get Value of d1 and store in buffer

	return(0);
}



////////////////////////////////////////////////////////////////////////////////
//
// Function     : client_lcloud_bus_request
// Description  : This the client regstateeration that sends a request to the 
//                lion client server.   It will:
//
//                1) if INIT make a connection to the server
//                2) send any request to the server, returning results
//                3) if CLOSE, will close the connection
//
// Inputs       : reg - the request reqisters for the command
//                buf - the block to be read/written from (READ/WRITE)
// Outputs      : the response structure encoded as needed

LCloudRegisterFrame client_lcloud_bus_request( LCloudRegisterFrame reg, void *buf ) {
	struct sockaddr_in addr;
	LCloudRegisterFrame buffer[7];
	LCloudRegisterFrame sending_reg;


	if(socket_handle == -1){									//if the socket handle is not initailized

		addr.sin_family = AF_INET;								//assign values to obtain address
		addr.sin_port = htons(LCLOUD_DEFAULT_PORT);


		int tmp = inet_aton(LCLOUD_DEFAULT_IP, &addr.sin_addr);	//convert to unix address
		if(tmp == 0){											//check that it was successful
			return(0);
		}
		socket_handle = socket(PF_INET, SOCK_STREAM, 0);		//create the socket
			if(socket_handle == -1) {							//check if it is valid
				return( -1);}  
		if( connect(socket_handle, (const struct sockaddr *)&addr, sizeof(addr)) == -1 ) { //make the connection to the server
			return( -1 );}  
	}

	extract_lcloud_register(reg, buffer);			//get the values of the register



	if(buffer[2] == LC_BLOCK_XFER){					//check if function is read or a write
		if(buffer[4] == LC_XFER_READ){
													//if it is a read 
			sending_reg = htonll64(reg);			//switch to network order
			write(socket_handle,&sending_reg,sizeof(sending_reg));		//send reg to serv
			read(socket_handle,&sending_reg,sizeof(sending_reg));		//read reg from server
			read(socket_handle,buf,LC_DEVICE_BLOCK_SIZE);				//read block
			sending_reg = ntohll64(sending_reg);	//switch to host order
			return(sending_reg); 	//return the reg
		}
		if(buffer[4] == LC_XFER_WRITE){
												//if it is a write
			sending_reg = htonll64(reg);		//switch to network order
			write(socket_handle,&sending_reg,sizeof(sending_reg));		//send reg
			write(socket_handle,buf,LC_DEVICE_BLOCK_SIZE);				//send block to write
			read(socket_handle,&sending_reg,sizeof(sending_reg));		//recieve a reg back
			sending_reg = ntohll64(sending_reg);	//switch to host order
			return(sending_reg);		//return reg

		}
	}

	if(buffer[2] == LC_POWER_OFF){
									//if power off
		sending_reg = htonll64(reg);
		write(socket_handle,&sending_reg,sizeof(sending_reg));		//send reg
		read(socket_handle,&sending_reg,sizeof(sending_reg));		//recieve reg
		close(socket_handle);	//close socket and set to -1
		socket_handle = -1;
		return(sending_reg);	//return reg

	}


	else{				//any other command such as probe...
		sending_reg = htonll64(reg);		//swtich to network order
		write(socket_handle,&sending_reg,sizeof(sending_reg));	//send reg
		read(socket_handle,&sending_reg,sizeof(sending_reg));	//recieve reg
		sending_reg = ntohll64(sending_reg);	//switch to host order
		return(sending_reg);	//return reg
	}
return(-1);		//if none were selected return -1 to indicate error
}




