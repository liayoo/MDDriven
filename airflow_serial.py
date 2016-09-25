import serial

filename = raw_input("Filename?:")

filename = filename + ".csv"

ser = serial.Serial('/dev/cu.PeterDevice-SPP', 115200, timeout=1)

text_file = open(filename, 'w')

while 1:
	if ser.inWaiting():
		line = ser.readline()
		print line.strip()
		text_file.write(line)
		
text_file.close()
ser.close()

