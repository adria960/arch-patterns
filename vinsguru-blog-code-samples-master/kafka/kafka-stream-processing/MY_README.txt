---------------------------------------------------------------------------------------------------------
START
	start cmd /k bin\windows\zookeeper-server-start.bat config\zookeeper.properties
	start cmd /k bin\windows\kafka-server-start.bat config\server.properties

STOP
	start cmd /k bin\windows\kafka-server-stop.bat
	start cmd /k bin\windows\zookeeper-server-stop.bat
---------------------------------------------------------------------------------------------------------
docker run -p 80:80 -e xeotek_kadeck_free="<your_email_address>" -e xeotek_kadeck_port=80 xeotek/kadeck:4.3.7

Log in with your favorite web browser via
http://localhost:80
with user & password:
admin