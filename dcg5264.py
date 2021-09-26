from random import randrange
from math import *

my_name = "Devin Gilmore"
userid = "dcg5264"


# also name your file userid.py

def gcd(a, b):
    # Your code here
    r: int = -1
    if a < b:
        a, b = b, a
    while r != 0:
        r = a % b
        if r == 0:
            return int(b)
        a = b
        b = r


def inverse(a, b):  # use extended euclidean algorithm to find inverse
    # Your code here

    fa = a
    fb = b
    last_s = 1
    s = 0
    r = -1
    while r != 0:
        r = a % b
        tmp = s
        s = last_s - ((a // b) * s)
        last_s = tmp
        a = b
        b = r
    return last_s


def generate_key(a, b):
    # Your code here
    n = a * b
    k = (a - 1) * (b - 1)
    e = randrange(1, k)
    while gcd(e, k) != 1:
        e = randrange(1, k)

    d = inverse(e, k)
    return (e, n), (d, n)
    # (e, n) is public, (d, n) is private
    # return ((e, n), (d, n))
    pass


def encrypt(public_key, txt):
    # Your code here
    i = 0
    tmpstr = ""
    dictto = {"a": "01", "b": "02", "c": "03", "d": "04", "e": "05", "f": "06", "g": "07", "h": "08", "i": "09",
              "j": "10", "k": "11", "l": "12", "m": "13",
              "n": "14", "o": "15", "p": "16", "q": "17", "r": "18", "s": "19", "t": "20", "u": "21", "v": "22",
              "w": "23", "x": "24", "y": "25", "z": "26"}

    for i in range(len(txt)):
        letter = dictto[txt[i]]
        tmpstr += letter
    tmpstr = tmpstr + "00000000"
    tmpstr = int(tmpstr)
    tmpstr = tmpstr ^ public_key[0] % public_key[1]

    return tmpstr


def decrypt(private_key, ciphers):
    # Your code here
    i = 0

    dictfrom = {1: "a", 2: "b", 3: "c", 4: "d", 5: "e", 6: "f", 7: "g", 8: "h", 9: "i", 10: "j", 11: "k", 12: "l",
                13: "m",
                14: "n", 15: "o", 16: "p", 17: "q", 18: "r", 19: "s", 20: "t", 21: "u", 22: "v",
                23: "w", 24: "x", 25: "y", 26: "z"}

    txt = ""
    tmpstr = ciphers
    tmpstr = tmpstr ^ private_key[0] % private_key[1]
    tmpstr = str(tmpstr)
    tmpstr = tmpstr[:-8]
    if len(tmpstr) % 2 != 0:
        tmpstr = "0" + tmpstr

    while i < len(tmpstr):
        letter = int(tmpstr[i] + tmpstr[i + 1])
        txt = txt + dictfrom[letter]
        i += 2
    return txt

    pass
