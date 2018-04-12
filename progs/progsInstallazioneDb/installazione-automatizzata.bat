@ECHO OFF

CLS

echo #######################################################################
echo #                        Municipia SpA                                #
echo #                                                                     #
echo #                        Installazione Automatizzata                  # 
echo #                                                                     #
echo #######################################################################
echo. 
echo. 
echo. 


java -Xms4096m -Xmx4096m -Xss512m -XX:PermSize=512m -XX:MaxPermSize=512m -jar resources/epe-1.0.7-jar-with-dependencies.jar -f resources/program-installazione-db-completa.epe resources/program-installazione-db-comuni-ambienti.epe


::set/p Premere invio per terminare
pause
exit
