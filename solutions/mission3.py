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

def get_next_line(sock):
    line = ''
    data = ''
    while data != '\n':
        line += data
        data = sock.recv(1).decode('utf-8')
    return line

def calc_distance(p1, p2):
    (x1, y1) = p1
    (x2, y2) = p2
    return math.sqrt( (x2 - x1)**2 + (y2 - y1)**2 )

def check_conditions(lat, lon, speed):
    distance = calc_distance( (lat, lon), (48.87378, 2.29497) ) 
    print(f"Distance: {distance}")

    return distance < 0.001 and speed < 15.0

def send_park(sock):
    message = '1a2#000000000000'
    sock.send(message.encode('utf-8'))

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
        line = get_next_line(sock)
        arb, data = parse_can(line)

        # Handle Messages

        # Latitude
        if arb == '01A3':
            lat = struct.unpack('<d', data)[0]
            print(f"Lat: {lat}")
        # Longitude
        elif arb == '01A4':
            lon = struct.unpack('<d', data)[0]
            print(f"Lon: {lon}")
        # Speed
        elif arb == '01A2':
            speed = int.from_bytes(data[:4], 'little') / 100.0
            print(f"Speed: {speed}")

        if check_conditions(lat, lon, speed):
            break

    # Put car into park
    send_park(udp_sock)

    # Clean up
    sock.close()
    udp_sock.close()

if __name__ == '__main__':
    main()
