@ECHO OFF
IF %1.==. GOTO No1
IF %2.==. GOTO No2
IF %3.==. GOTO No3

set path=%path%;C:\Users\Dali\Documents\Cours\Projet_Recherche\javacc-6.0\bin
set path=%path%;C:\Program Files (x86)\Java\jdk1.7.0_80\bin
set NameJJ=%1
set NameClass=%2
set PathFile=%3

ECHO ----------------------------Building Java Files-----------------------------
call javacc %NameJJ%
ECHO ----------------------------Compiling Java Files----------------------------
call javac *.java
ECHO ----------------------------Running Test----------------------------
call java %NameClass% < %PathFile%

GOTO End1

:No1
  ECHO No param 1
GOTO End1
:No2
  ECHO No param 2
GOTO End1
:No3
  ECHO No param 3
GOTO End1

:End1