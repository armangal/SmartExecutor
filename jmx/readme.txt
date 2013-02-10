 In windows systems you may get an exception related to file access permissions. 
 To solve this exceptions use the command 
 
 cacls jmx.password /P <username>:R 
 
 and 
 
 cacls jmx.access /P <username>:R
 
 On Linux:
 chmod 400 jmxremote.password
 or
 chmod 600 jmxremote.password
 
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=5555
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=true
-Dcom.sun.management.jmxremote.access.file=d:/jmx/jmx.access
-Dcom.sun.management.jmxremote.password.file=d:/jmx/jmx.password