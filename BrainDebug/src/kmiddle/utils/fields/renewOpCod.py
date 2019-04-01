#! /usr/bin/python
import os
d = {'A':'0000','B':'0000','C':'0001','D':'0001','E':'0010','F':'0010',
'G':'0011','H':'0011','I':'0100','J':'0100','K':'0101','L':'0101',
'M':'0110','N':'0110','O':'0111','P':'0111','Q':'1000','R':'1000',
'S':'1001','T':'1001','U':'1010','V':'1010','W':'1011','X':'1011',
'Y':'1100','Z':'1100'}

letras = [['A','B'],['C','D'],['E','F'],['G','H'],['I','J'],['K','L'],
['M','N'],['O','P'],['Q','R'],['S','T'],['U','V'],['W','X'],['Y','Z']]

path = 'OperationCodeConstants.java'
def removeFile():
	f = open(path,'r')
	listas = f.readlines()
	f.close()
	os.remove(path)
	return listas


def completa(valor):
	sinB = valor[2:]
	return (4 - sinB.__len__())*'0' + sinB
	
def codificaCodop(codop):
	listaCodops = codop.split('_')
	cadenaBinaria = ""
	for elem in listaCodops:
		cadenaBinaria = cadenaBinaria + d[elem[0]]
	return cadenaBinaria

def modificaValores(listas):
	j = 0
	letra = letras[j]
	contador = 0
	for i in range(0,listas.__len__()):
		if listas[i].__contains__('final short'):
			elementos = listas[i].split()
			while not letra.__contains__(elementos[2][0]):
				j = j + 1
				letra = letras[j]
				contador = 0
			contador = contador + 1 
				
			cadenaB = codificaCodop(elementos[2])
			nuevoValor = '0b'+cadenaB + completa(bin(contador))
			valorEntero  = int(nuevoValor,2)
			listas[i] = '\tfinal short '+elementos[2]+ ' = '+str(valorEntero)+';\n'	
	file = open(path,'w')
	file.writelines(listas)
	file.close()
	

def main():
	lista = removeFile()
	modificaValores(lista)
	

if __name__ == "__main__":
	main()
