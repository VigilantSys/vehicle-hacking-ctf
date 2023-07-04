import math
import socket
import struct

def parse_can(line):
    parts = line.split('\t')
    arb = parts[1]
    data = parts[3]
    data_parts = data.split(' ')
    data_bytes = bytearray.fromhex(''.join(data_parts))
    return arb, data_bytes

def main():
    # Initialize tcp
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = ('127.0.0.1', 5555)
    sock.settimeout(1.5)
    sock.connect(server_address)

    # Initialize udp
    udp_sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_addr = ('127.0.0.1', 5000)
    udp_sock.connect(udp_addr)

    lat = 0.0
    lon = 0.0
    speed = 0.0
    distance = 99999.99

    while True:
        # Get CAN message
        line = ''
        data = ''
        while data != '\n':
            line += data
            data = sock.recv(1).decode('utf-8')

        arb, data = parse_can(line)

        if arb == '01A3':
            lat = struct.unpack('<d', data)[0]
            print(f"Lat: {lat}")
        elif arb == '01A4':
            lon = struct.unpack('<d', data)[0]
            print(f"Lon: {lon}")
        elif arb == '01A2':
            speed = int.from_bytes(data[:4], 'little') / 100.0
            print(f"Speed: {speed}")

        distance = math.sqrt( (48.87378 - lat)**2 + (2.29497 - lon)**2 )
        print(f"Distance: {distance}")

        if distance < 0.001 and speed < 15.0:
            break

    message = '1a2#000000000000'
    udp_sock.send(message.encode('utf-8'))


    sock.close()
    udp_sock.close()

if __name__ == '__main__':
    main()
