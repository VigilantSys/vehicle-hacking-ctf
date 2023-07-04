import random
from geopy.distance import geodesic as GD

coords='''2.2717368,48.8666776,
2.2731959,48.8685126,
2.274655,48.8704039,
2.2750842,48.8710814,
2.2757279,48.8715612,
2.2770153,48.8718435,
2.279719,48.8721258,
2.2831093,48.8724927,
2.2857272,48.8728032,
2.2879158,48.8730573,
2.2902762,48.8732549,
2.2933661,48.8736218,
2.2942244,48.8737347,
2.2949969,48.8733395,
2.2956835,48.8735089,
2.2958123,48.8740452,
2.2956406,48.8746097,
2.2968676,48.8761271,
2.2978976,48.8776512,
2.2962668,48.8781592,
2.294636,48.8785543,
2.2927478,48.8790624,
2.2903445,48.8795704,
2.2880271,48.8801348,
2.285538,48.8786108,
2.283478,48.8771996,
2.2818473,48.8759577,
2.2795298,48.8751674,
2.2779849,48.8742077,
2.2761824,48.872627,
2.2744658,48.8720625,
2.2737792,48.8711028,
2.2714618,48.8695784,
2.2696593,48.8679976,
2.2680285,48.8655133,
2.2669986,48.8642712,
2.2675135,48.8633677,
2.2690585,48.8633677,
2.2700885,48.8648923,'''


def calc_distance(p1, p2):
    return GD(p1,p2).mi

def calc_speed(distance, ticktime):
    multiplier = (60.0 * 60.0) / ticktime
    return (distance * multiplier) / ticktime

def get_tuple(line):
    items = line.split(',')
    return (items[0], items[1])

def calc_rpm(speed):
    speed_conv = speed / 2.23694 # To m/s
    wheel_diam = 0.2
    RPM = (60.0 * speed_conv)/(3.14 * wheel_diam)
    return RPM

def calc_fuel(previous, distance):
    efficiency = 31.0 # mpg
    tank_size = 10.0 # gal
    newval = previous - (((distance / efficiency) / tank_size) * 100)
    return newval

def calc_temp(base):
    offset = random.uniform(-1.3,0.8)
    return base + offset


tick = 3.5 # seconds
lines = coords.split('\n')
current_fuel = 67.82934 # percent
base_temp = 71.0
for index, line in enumerate(lines):
    if index == 0:
        distance = calc_distance(get_tuple(lines[-1]), get_tuple(lines[index]))
    else:
        distance = calc_distance(get_tuple(lines[index-1]), get_tuple(lines[index]))
    lon, lat = get_tuple(line)
    speed = calc_speed(distance, tick)
    rpm = calc_rpm(speed)
    fuel = calc_fuel(current_fuel, distance)
    current_fuel = fuel
    temp = calc_temp(base_temp)
    radio = "95.5 FM"
    gear = "drive"
    newline = f"{speed:.2f},{rpm:.0f},{fuel:.2f},{temp:.2f},{lon},{lat},{radio},{gear}"
    print(newline)
    #print(f"Went {distance} mi at {speed} mph and RPM {rpm}. Fuel left {fuel}%. Temperature if {temp} F")

    
