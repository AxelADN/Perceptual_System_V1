#! /usr/bin/python
import os
path = 'BigNodesList.kua'
path2 = 'BigNodeNameConstants.java'
path3 = 'BigNodeNameStrings.java'

def removeFile():
	f = open(path,'r')
	listas = f.readlines()
	f.close()
	os.remove(path2)
	return listas

def completa(valor):
	sinB = valor[2:]
	return (16 - sinB.__len__())*'0' + sinB
	

def modificaValores(listas):
	contador = 0
	for i in range(0,listas.__len__()):
		if listas[i].__contains__('final int'):
			elementos = listas[i].split()
			cadenaB = '0b'+ completa(bin(contador)) + 16*'0'
			contador = contador + 1
			valorEntero  = int(cadenaB,2)
			listas[i] = '\tfinal int '+elementos[2]+ ' = '+str(valorEntero)+';\n'	
	file = open(path2,'w')
	file.writelines(listas)
	file.close()
	
def arreglarSentencia(lista):
	for i in range(0,lista.__len__()):
		item = lista[i]
		item  = item[:-1].replace(' ','_')
		item = item.upper()
		lista[i] = item
	return lista	
		
def creaClase(lista):
	lineas = []
	lineas.append('package kmiddle.utils.fields;\n')
	lineas.append('public interface BigNodeNameConstants {\n')
	lineas = lineas + lista	
	lineas.append('}')
	file = open(path2,'w')
	file.writelines(lineas)
	file.close()
	
def agregaValores(listas):
	lineas = []
	contador = 0
	for i in range(0,listas.__len__()):
		elemento = listas[i]
		cadenaB = '0b'+ completa(bin(contador)) + 16*'0'
		contador = contador + 1
		valorEntero  = int(cadenaB,2)
		lineas.append('\tfinal int '+elemento+ ' = '+str(valorEntero)+';\n')	
	return lineas

def creaNombres(lista):
	os.remove(path3)
	lineas = []
	lineas.append('package kmiddle.utils.fields;\n');
	lineas.append('public class BigNodeNameStrings implements BigNodeNameConstants{\n')
	lineas.append('\tpublic static String getBigNodeName(int name){\n')
	lineas.append('\t\tswitch(name) {\n')
	
	for i in range(0,lista.__len__()):
		item = lista[i]
		lineas.append('\t\t\tcase '+item+': return \"'+item+'\";\n')
	lineas.append('\t\t\tdefault: return \"SMALLNODE[\"+name+\"]\";\n')
	lineas.append('\t\t}\n')
	lineas.append('\t}\n')
	lineas.append('}\n')
	file = open(path3,'w')
	file.writelines(lineas)
	file.close()

def main():
	lista = removeFile()
	listamayuscula = arreglarSentencia(lista)
	listavalores = agregaValores(listamayuscula)
	listanombres = creaNombres(listamayuscula)
	creaClase(listavalores)
	
	
if __name__ == "__main__":
	main()
