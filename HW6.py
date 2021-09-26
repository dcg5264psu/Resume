#HW 6
#Due Date: 04/26/2019, 11:59PM
########################################
#
# Name:Devin Gilmore
# Collaboration Statement:i worked on this assignment alone.
#
########################################

# ---Copy your code from labs 10 and 11 here (you can remove their comments)  



#----- HW6 Graph code

class Node:
    def __init__(self, value):
        self.value = value  
        self.next = None 
    
    def __str__(self):
        return "Node({})".format(self.value) 

    __repr__ = __str__
                          

class Stack:
    def __init__(self):
        self.top = None
    
    def __str__(self):
        temp=self.top
        out=[]
        while temp:
            out.append(str(temp.value))
            temp=temp.next
        out='\n'.join(out)
        return ('Top:{}\nStack:\n{}'.format(self.top,out))

    __repr__=__str__
    
    def isEmpty(self):
        #write your code here
        if self.top == None:
            return True
        else:
            return False
    def __len__(self):
        #write your code here
        current = self.top
        count = 1
    #Take into consideration if the list is empty.
        if self.isEmpty() == True:
            return 0
    #Run through the list and count the entries.
        else:
            while current.next != None:
                current = current.next
                count += 1
            return count



    def push(self,value):
        #write your code here
    #start the list by just setting the value to top.
        if self.isEmpty() == True:
            self.top = Node(value)
    #every additional item is put on top and the next is set to the previous top.
        else:
            current = Node(value)
            current.next = self.top
            self.top = current

    def pop(self):
        #write your code here
        #If the list is empty then return error.
        if self.isEmpty() == True:
            return "Error:Stack is empty"
        #Remove first element and return it.
        else:
            current = self.top
            self.top = current.next
            return current.value
    

    def peek(self):
        return self.top.value



class Queue:

    def __init__(self): 
        self.head=None
        self.tail=None

    def __str__(self):
        temp=self.head
        out=[]
        while temp:
            out.append(str(temp.value))
            temp=temp.next
        out=' '.join(out)
        return ('Head:{}\nTail:{}\nQueue:{}'.format(self.head,self.tail,out))

    __repr__=__str__

    def isEmpty(self):
        #write your code here
        if self.head == None:
            return True
        else:
            return False
    def __len__(self):
        #write your code here
        current = self.head
        count = 1
    #Take into consideration if the list is empty.
        if self.isEmpty() == True:
            return 0
    #Run through the list and count the entries.
        else:
            while current.next != None:
                current = current.next
                count += 1
            return count
    def enqueue(self, value):
        #write your code here
        current = Node(value)
#if the list is empty for the first value.
        if self.isEmpty() == True:
        	self.head = current
        	self.tail = current
#set last value to the new node.
        else:
        	temp = self.head
        	while temp.next != None:
        		temp = temp.next
        	temp.next = current
#update the head of the list
        	self.tail = current

    def dequeue(self):
        #write your code here
        current = self.head
#if there is only one element left.
        if self.__len__() == 1:
        	self.head = None
        	self.tail = None
#if there are no value in the list return error.
        elif self.isEmpty() == True:
        	return 'Error:Queue is empty'
#if there is remove first element and update the tail.
        else:
        	poppedvalue = self.head
        	self.head = current.next
        	return poppedvalue.value
     
class Graph:
    def __init__(self, graph_repr):
        self.vertList = graph_repr


    def bfs(self, start):
    	# Your code starts here
        #Define variables.
        q = Queue()
        q.enqueue(start)
        visited = []
        visited.append(start)
        #Make sure the list is not empty.
        while q.isEmpty() == False:
            temp = q.head.value
            node = q.dequeue()
            #Search the graph as long as it hasnt been visited.
            if node not in visited:
                if node != None:
                    visited.append(node)
                #Assign the value of the node to the visited list.
            for item in sorted(self.vertList[temp]):
                if type(item) == tuple:
                    item = item[0]
                if item not in visited:
                    if item != None:
                        q.enqueue(item)
        return visited

    def dfs(self, start):
    	# Your code starts here
        #Define vaiables.
        s = Stack()
        visited = []
        visited.append(start)
        s.push(start)
        #Make sure the list is not empty. 
        while s.isEmpty() == False:
            count = 0
            current = s.top.value
            #Search the list making sure it hasnt been visited.
            for item in sorted(self.vertList[current]):
                if type(item) == tuple:
                    item = item[0]
                if item not in visited:
                    visited.append(item)	
                    s.push(item)
                    count += 1
                    break
            if count == 0:
                s.pop()
        return visited


    ### EXTRA CREDIT, uncomment method definition if submitting extra credit
    
    def dijkstra(self,start):
        # Your code starts here
        q = Queue()
        done = {}
        visited = {}
        done[start] = 0
        q.enqueue(start)

#set the weights for when traversing graph.
        while q.isEmpty() == False:
            temp = q.dequeue()

            visited[temp] = done[temp]
#Retrieve the actual weight.
            for item in sorted(self.vertList[temp]):
                if type(item) == tuple:
                    item = item[0]
                    weight = item[1]
                else:
                    return "error"
                #Assign an inital value to the node.
                if item not in done:
                    done[item] = done[current] + cost
                    q.enqueue(item)
                #minimize the node in order to find smallest value. 
                elif done[item] > (done[current] + cost):
                    done[item] = done[current] + cost
                    q.enqueue(item)

        return visited
