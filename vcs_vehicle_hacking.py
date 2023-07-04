from colorama import init, Fore, Back, Style
from tabulate import tabulate
import subprocess
import math
import enum
import can
import signal
import sys
import socket
import time
import binascii
import struct

def signal_handler(sig, frame):
    subprocess.call(['sudo', 'docker', 'kill', 'mission1'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    subprocess.call(['sudo', 'docker', 'kill', 'mission2'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    subprocess.call(['sudo', 'docker', 'kill', 'mission3'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    sys.exit(0)


class Events(enum.Enum):

    MAIN_MENU = "MAIN_MENU"
    NEXT_MISSION = "NEXT_MISSION"
    PLAYING_MISSION = "PLAYING_MISSION"
    QUIT = "QUIT"
    VICTORY = "VICTORY"

class Mission:

    def __init__(self, missionid, title, description):
        self.missionid = missionid
        self.title = title
        self.description = description

    def start(self):
        print()
        self.prerun()
        print()
        print(Fore.RED + Style.BRIGHT + f'Mission {self.missionid}: {self.title}')
        print(self.description)
        return self.run()

    def prerun(self):
        pass 

    def run(self):
        pass

class Intro(Mission):

    def __init__(self):
        
        desc_table = [[f'{Style.BRIGHT}Target Name{Style.RESET_ALL}',f'{Style.BRIGHT}Target Description{Style.RESET_ALL}',f'{Style.BRIGHT}Background{Style.RESET_ALL}'],
                      ["Harold Grimwald",
                       "Mafia Crime Boss",
                       "Our target is a notorious mafia strongman. Our legal team has finally built a case with enough evidence to take him down for good. But bringing a crime boss into custody isn't as simple as knocking on his front door. We need to nab him without starting a shootout, but he nearly always hides out in his ultra-secure compound with all his cronies.\nOur intelligence agents have received a tip that he will make a rare trip out of his compound to meet with a rival boss. We want to capture him en-route to this meeting, but we can't risk him getting suspicious and avoiding our trap. To ensure the mission goes smoothly, the boss has approved a risky undercover mission. We need you to go undercover to infiltrate the target's compound and upload a payload into his vehicle that will allow us to disable it at the right moment to spring our trap.\nThe lives our your fellow agents are in your hands. Good luck!"]]
        desc = tabulate(desc_table, headers='firstrow', tablefmt='fancy_grid', maxcolwidths=[20,20,40])
        super().__init__(0, "Intro", desc)

    def run(self):
        response = input(f"{Style.BRIGHT}If you accept this mission, respond with \"y\": ")
        if response.lower() == 'y':
            return Events.NEXT_MISSION
        else:
            return Events.MAIN_MENU

class Mission1(Mission):

    def __init__(self):

        desc_table = [[f'{Style.BRIGHT}Location{Style.RESET_ALL}', f'{Style.BRIGHT}Objective{Style.RESET_ALL}', f'{Style.BRIGHT}Supporting Data{Style.RESET_ALL}'],
                     ["Undercover in target's garage within his mafia compound.",
                      "Enter the target's vehicle without leaving a trace.",
                      "Unfortunately, the vehicle's specific protocol is proprietary, but our open-source intelligence agents were able to find some information through online vehicle forums. We have provided this information in the ./mission1/unlock_via_can.png file. Take a look before you start the mission. Then, pop out the back headlight and jack into the CAN bus."]]
        desc = tabulate(desc_table, headers='firstrow', tablefmt='fancy_grid', maxcolwidths=[20,20,40])
        super().__init__(1, "I'm in!", desc)

    def prerun(self):
        print(f"{Style.BRIGHT}Initializing...")
        
        # Initialize can interface
        subprocess.call(['sudo', 'modprobe', 'vcan'])
        subprocess.call(['sudo', 'ip', 'link', 'add', 'dev', 'vcan0', 'type', 'vcan'])
        subprocess.call(['sudo', 'ip', 'link', 'set', 'up', 'vcan0'])

        # Start Docker container here
        subprocess.call(['sudo', 'docker', 'build', '-f', './setup/mission1/Dockerfile.m1', '--network=host', '-t', 'cantrain/m1', './'])
        docker_proc = subprocess.Popen(['sudo', 'docker', 'run', '--rm', '--network=host', '--name', 'mission1', 'cantrain/m1'], stdin=subprocess.DEVNULL, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    def run(self):

        print(f"{Style.BRIGHT}All right! You're in the garage. Now pop out the headlight and connect to the CAN bus on interface vcan0.")

        self.wait_for_unlock()
        subprocess.call(['sudo', 'docker', 'kill', 'mission1'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        return Events.NEXT_MISSION

    def wait_for_unlock(self):
        with can.interface.Bus('vcan0', bustype='socketcan') as can_bus:

            while True:
                message = can_bus.recv()
                if message.arbitration_id == 0x1d4:
                    if message.data[2] == 0x01:
                        print(f'{Style.BRIGHT}{Fore.GREEN}MISSION SUCCESS: Car unlocked!')
                        break
                    elif message.data[2] != 0x00:
                        print(f"{Fore.YELLOW}I see some anomalous data, but the car didn't unlock. Keep trying.")

class Mission2(Mission):
    
    def __init__(self):

        desc_table = [[f'{Style.BRIGHT}Location{Style.RESET_ALL}', 
                       f'{Style.BRIGHT}Objective{Style.RESET_ALL}', 
                       f'{Style.BRIGHT}Supporting Data{Style.RESET_ALL}'],
                      ["In the driver's seat of the target's vehicle.",
                       "Upload a back door to the vehicle's infotainment system.",
                       "We downloaded the firmware for the vehicle's infotainment system from the Roylls Roce support website, but our analysts couldn't do much with it. Hopefully you'll have better luck. Check out the firmware. It's located in ./mission2/roylls_roce_ghoul_vii_firmware_update_2018. When you're ready, upload your modifications to the infotanment system by adding them to the USB drive mounted in ./mission2/USB"]]

        desc = tabulate(desc_table, headers='firstrow', tablefmt='fancy_grid', maxcolwidths=[20,20,40])
        super().__init__(2, "In through the backdoor", desc)

    def prerun(self):
        print(f"{Style.BRIGHT}Initializing...")
        
        # Restart Docker service
        subprocess.call(['sudo', 'service', 'docker', 'restart'])

    def run(self):
        print(f"{Style.BRIGHT}Great! You made it in the car. Start analyzing that firmware, and upload it to the infotainment system.")
        response = input(f"{Style.BRIGHT}When you've finished modifying the firmware, put into onto the USB drive and type y: ")
        if response.lower() != 'y':
            return Events.MAIN_MENU

        # Build and start Docker container
        print(f"{Style.BRIGHT}Uploading firmware image...")
        error = subprocess.call(['sudo', 'docker', 'build', '-f', './setup/mission2/Dockerfile.m2', '-t', 'cantrain/m2', './'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        if error != 0:
            print(f"{Style.BRIGHT}{Fore.RED}MISSION FAILED: The firmware you uploaded was not accepted by the infotainment system.")
            subprocess.call(['sudo', 'docker', 'kill', 'mission2'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            return Events.MAIN_MENU

        print(f"{Fore.GREEN}Firmware successfully uploaded")
        print(f"{Style.BRIGHT}Attempting to execute firmware...")
        docker_proc = subprocess.Popen(['sudo', 'docker', 'run', '--rm', '-p', '127.0.0.1:5555:5555/tcp', '--name', 'mission2', 'cantrain/m2'], stdin=subprocess.DEVNULL, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        success = self.wait_for_tcp()
        subprocess.call(['sudo', 'docker', 'kill', 'mission2'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        return success

    def wait_for_tcp(self):
        # Give time for interface to come up
        time.sleep(5)

        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = ('127.0.0.1', 5555)
        connected = self.connect_to_socket(sock, server_address)
        if not connected:
            print(f"{Style.BRIGHT}{Fore.RED}MISSION FAILED: The firmware was executed but no backdoor appeared.")
            return Events.MAIN_MENU
        print(f"{Style.BRIGHT}{Fore.GREEN}MISSION SUCCESS: The firmware you uploaded spawned a backdoor server. Good work!")
        return Events.NEXT_MISSION

    def connect_to_socket(self, sock, addr):
        for i in range(3):
            try:
                sock.settimeout(1.5)
                sock.connect(addr)
                return True
            except Exception as e:
                time.sleep(1.5)
                continue
        return False
        

class Mission3(Mission):

    def __init__(self):

        desc_table = [[f'{Style.BRIGHT}Location{Style.RESET_ALL}',
                       f'{Style.BRIGHT}Objective{Style.RESET_ALL}',
                       f'{Style.BRIGHT}Supporting Data{Style.RESET_ALL}'],
                      ["On an agency computer within the portable lab near the target's compound.",
                       "When the target's vehicle enters the specified area, disable it using the backdoor in the implanted infotainment system firmware.",
                       "Good news! Our agents found an ICD of the vehicle's CAN bus traffic. It's located at ./mission3/Roylls Roce Ghoul VII CAN Bus ICD.pdf. The goal is to put the vehicle into park when the it is within a radius of 0.001 degrees of the point 48.87378,2.29497. But be careful! The car can only be put into park when it is travelling less than 15 mph, and the message to change the gear has a powerful security mechanism called a checksum that you'll have to figure out. If you attempt to change the gear at the wrong location or speed, the target's driver will know something is up, and we will have to abort mission. Go ahead and connect to the backdoor ports at 127.0.0.1:5000/udp and 127.0.0.1:5555/tcp. Good luck agent! It's showtime."]]

        desc = tabulate(desc_table, headers='firstrow', tablefmt='fancy_grid', maxcolwidths=[20,20,40])
        super().__init__(3, "Showtime!", desc)

    def prerun(self):
        print(f"{Style.BRIGHT}Initializing...")
        
        # Restart Docker service
        subprocess.call(['sudo', 'service', 'docker', 'restart'])

        # Start Docker container here
        subprocess.call(['sudo', 'docker', 'build', '-f', './setup/mission3/Dockerfile.m3', '-t', 'cantrain/m3', './'])
        docker_proc = subprocess.Popen(['sudo', 'docker', 'run', '--rm', '-p', '127.0.0.1:5555:5555/tcp', '-p', '127.0.0.1:5000:5000/udp', '--name', 'mission3', 'cantrain/m3'], stdin=subprocess.DEVNULL, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    def run(self):
        print(f"{Style.BRIGHT}Remember, the backdoor you implanted is at 127.0.0.1:5000/udp and 127.0.0.1:5555/tcp. And don't mess up!")

        result = self.wait_for_park()
        subprocess.call(['sudo', 'docker', 'kill', 'mission3'], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        return result

    def wait_for_park(self):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_address = ('127.0.0.1', 5555)

        # Initialize can state
        self.state = { 'speed': 0.0,
                       'prev_speed': 0.0,
                       'lat': 0.0,
                       'lon': 0.0,
                       'gear': 'drive',
                       'checksum_valid': True}
        
        self.connect_to_socket(sock, server_address)

        while True:
            connection_broken = False
            line = ''
            data = ''
            while data != '\n':
                line += data
                try:
                    data = sock.recv(1).decode('utf-8')
                    if data == '':
                        print('No data')
                        self.reconnect(sock, server_address)
                        connection_broken = True
                        break
                except Exception as e:
                    print(e)
                    self.reconnect(sock, server_address)
                    connection_broken = True
                    break
            if connection_broken:
                continue
            arbID, data = self.parse_can(line)
            self.handle_can(arbID, data)
            success = self.check_success_conditions()
            if success is not None:
                return success

    def reconnect(self, sock, addr):
        print(f"{Style.BRIGHT}Connection to server lost, reconnecting...")
        sock.close()
        self.connect_to_socket(sock, addr)
        print(f"{Style.BRIGHT}Connection reestablished.")

    def connect_to_socket(self, sock, addr):
        while True:
            try:
                #set_keepalive_linux(sock)
                sock.settimeout(1.5)
                sock.connect(addr)
                # Keep socket open
                #sock.sendall(b'EHLO')
                #sock.shutdown(socket.SHUT_WR)
                break
            except Exception as e:
                time.sleep(1.5)
                continue

    def check_success_conditions(self):
        if not self.state['checksum_valid']:
            print(f"{Style.BRIGHT}{Fore.RED}MISSION FAILED:{Style.NORMAL} A CAN bus security error tipped off the target's driver to our trap, and he escaped.")
            return Events.MAIN_MENU
        if not self.state['gear'] == 'park':
            return None
        
        if self.state['prev_speed'] > 15.0:
            print(f"{Style.BRIGHT}{Fore.RED}MISSION FAILED:{Style.NORMAL} The car attempted to change gears, but the speed was too high causing an error that was noticed by the target's driver.")
            return Events.MAIN_MENU

        if not self.in_range(self.state['lat'], self.state['lon']):
            print(f"{Style.BRIGHT}{Fore.RED}MISSION FAILED:{Style.NORMAL} The car shifted into park in the wrong location. The target slipped away before our agents could arrive.")
            return Events.MAIN_MENU

        print(f"{Fore.GREEN}The Vehicle was stopped while going {self.state['prev_speed']:0.2f} at {self.state['lat']},{self.state['lon']}. Our agents were able to take the target into custody!")
        return Events.VICTORY 

    def in_range(self, lat, lon):

        succ_lat = 48.87378
        succ_lon = 2.29497
        radius = 0.001

        distance = math.sqrt( (succ_lat - lat)**2 + (succ_lon - lon)**2 )

        return distance <= radius


    def parse_can(self, line):
        parts = line.split('\t')
        if len(parts) != 4:
            return ('', b'')
        arbID = parts[1]
        data = parts[3]
        data_parts = data.split(' ')

        try:
            data_bytes = bytearray.fromhex(''.join(data_parts))
        except Exception as e:
            return (arbID, b'')
        return (arbID, data_bytes)

    def handle_can(self, arbID, data):
        if arbID == '01A2':
            self.handle_speed_gear(data)
        elif arbID == '01A3':
            self.handle_latitude(data)
        elif arbID == '01A4':
            self.handle_longitude(data)
        else:
            return
        #print(self.state)

    def anomalous_data(self):
        print(f'{Fore.YELLOW}I see some anomoulous data on the bus, but there was not effect on the vehicle.')

    def handle_coordinate(self, data):
        if len(data) != 8:
            self.anomalous_data()
            return

        try:
            coord = struct.unpack('<d', data)[0]
        except Exception as e:
            print(e)
            self.anomalous_data()
            return

        return coord

    def handle_latitude(self, data):
        latitude = self.handle_coordinate(data)
        if latitude is None:
            return
        self.state['lat'] = latitude

    def handle_longitude(self, data):
        longitude = self.handle_coordinate(data)
        if longitude is None:
            return
        self.state['lon'] = longitude

    def handle_speed_gear(self, data):
            if len(data) != 6:
                self.anomalous_data()
                return

            speed_data = data[:4]
            gear_data = data[4]
            checksum_data = data[5]

            # Get Speed
            try:
                speed = int.from_bytes(speed_data, 'little')
                speed /= 100.0
            except Exception as e:
                self.anomalous_data()
                return

            # Get gear
            try:
                gear = int(gear_data)
                if gear == 0:
                    gear = 'park'
                elif gear == 1:
                    gear = 'reverse'
                elif gear == 2:
                    gear = 'neutral'
                elif gear == 3:
                    gear = 'drive'
                elif gear == 4:
                    gear = 'low'
                else:
                    self.anomalous_data()
                    return
            except Exception as e:
                self.anomalous_data()

            # Get checksum
            try:
                checksum = int(checksum_data)
            except Exception as e:
                self.anomalous_data()
                return

            # Validate checksum
            try:
                valid = self.validate_checksum(data[:-1], checksum)
            except Exception as e:
                self.anomalous_data()
                return

            self.state['prev_speed'] = self.state['speed']
            self.state['speed'] = speed
            self.state['gear'] = gear
            self.state['checksum_valid'] = valid
            
    def validate_checksum(self, data, checksum):
        summ = 0
        for b in data:
            summ += int(b)
        summ %= 256
        
        return summ == checksum

# Helpers
def set_keepalive_linux(sock, after_idle_sec=1, interval_sec=3, max_fails=5):
    """Set TCP keepalive on an open socket.

    It activates after 1 second (after_idle_sec) of idleness,
    then sends a keepalive ping once every 3 seconds (interval_sec),
    and closes the connection after 5 failed ping (max_fails), or 15 seconds
    """
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_KEEPALIVE, 1)
    #sock.setsockopt(socket.SOL_SOCKET, socket.SO_LINGER,
    #             struct.pack('ii', l_onoff, l_linger))
    sock.setsockopt( socket.SOL_SOCKET,
                     socket.SO_REUSEADDR, 1 )
    sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_KEEPIDLE, after_idle_sec)
    sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_KEEPINTVL, interval_sec)
    sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_KEEPCNT, max_fails)
        

class Game():

    def __init__(self):
        self.missions = [Intro(), Mission1(), Mission2(), Mission3()]
        self.mission = -1
        self.state = Events.MAIN_MENU
        self.loop()

    def main_menu(self):
        print()
        print('Welcome to Vigilant Cyber Systems Vehicle Hacking Training!')
        print('\t0. Play Intro')
        print('\t1. Play Mission 1')
        print('\t2. Play Mission 2')
        print('\t3. Play Mission 3')
        print('\t4. Quit')
        print()
        response = input(f'{Style.BRIGHT}Choose an option from the menu: ')
        if response == '4':
            return Events.QUIT
        elif response in ['0', '1', '2', '3']:
            self.mission = int(response) - 1
            return Events.NEXT_MISSION
        else:
            return Events.MAIN_MENU

    def loop(self):
        if self.state == Events.MAIN_MENU:
            self.mission = 0
            self.state = self.main_menu()
        elif self.state == Events.NEXT_MISSION:
            self.mission += 1
            if len(self.missions) <= self.mission:
                self.state = Events.QUIT
            else:
                self.state = self.missions[self.mission].start()
        elif self.state == Events.QUIT:
            print(f"{Style.BRIGHT}Thank you for training with us. Goodbye!")
            return
        elif self.state == Events.VICTORY:
            print(f"{Style.BRIGHT}{Fore.GREEN}Congratulations on completing the Vigilant Cyber Systems Vehicle Hacking Training Program!")
            self.state = Events.MAIN_MENU
        else:
            print(f"{Style.BRIGHT}{Fore.RED}Error occurred: Invalid state")
            return
        self.loop()


def main():
    init(autoreset=True)
    signal.signal(signal.SIGINT, signal_handler)

    game = Game()

if __name__ == '__main__':
    main()
