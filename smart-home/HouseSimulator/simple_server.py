import socket
import traceback
import sys
import threading

HEATER = "Heater"
CHILLER = "Chiller"

class HouseState(object):
   '''
   Model the house state
   '''
   def __init__(self):
      '''
      Initialize the house state
      '''
      self.__door = True
      self.__lock = False
      self.__light = True
      self.__resident_proximity = True
      self.__intruder_proximity = False
      self.__alarm_active = False
      self.__alarm_state = False
      self.__heater_state = True
      self.__heater_state = True
      self.__chiller_state = False
      self.__dehumidifier = False
      self.__heater_state = False
      self.__chiller_state = False
      self.__temperature = 65
      self.__humidity = 90
      self.__hvac_mode = HEATER

      self.__param = ";"
      self.__end = "."

   def update_simulation(self):
      '''
      Update the house simulation. This is really very simple
      '''
      if self.__heater_state: self.__temperature += 1
      if self.__chiller_state: self.__temperature -= 1

      if self.__humidity<100 and self.__humidity>0:
         if self.__dehumidifier:
            self.__humidity -=1
         else:
            self.__humidity +=1

   def set_state(self, new_state):
      '''
      Handle set state requests
      '''
      print ('Received state update {0}:'.format(new_state[3:-1]))
      params = new_state[3:-1].split(';')
      #
      for par in params:
         if len(par) == 0: continue
         k,v = par.split('=')
         if k == "LS":
            if v == "1": self.__light = True
            else: self.__light = False
         elif k == "AS":
            if v == "1": self.__alarm_state = True
            else: self.__alarm_state = False
         elif k == "AA":
            if v == "1": self.__alarm_active = True
            else: self.__alarm_active = False
         elif k == "DS":
            if v == "1": self.__door = True
            else: self.__door = False
         elif k == "LOS":
            if v == "1": self.__lock = True
            else: self.__lock = False
         elif k == "HUS":
            if v == "1": self.__dehumidifier = True
            else: self.__dehumidifier = False
         elif k == "RP":
            if v == "1": self.__resident_proximity = True
            else: self.__resident_proximity = False
         elif k == "IP":
            if v == "1": self.__intruder_proximity = True
            else: self.__intruder_proximity = False
         elif k == "HES":
            if v == "1": self.__heater_state = True
            else: self.__heater_state = False
         elif k == "CHS":
            if v == "1": self.__chiller_state = True
            else: self.__chiller_state = False
         elif k == "HM":
            if v == "1": self.__hvac_mode = HEATER
            else: self.__hvac_mode = CHILLER

   # Getters and setters for house properties
   def get_temperature(self): return self.__temperature

   def get_humidity(self): return self.__humidity

   def set_door(self, d): self.__door = d
   def get_door(self):
      if self.__door: return "1"
      return "0"

   def set_lock(self, l): self.__lock = l
   def get_lock(self):
     if self.__lock: return "1"
     return "0"


   def set_light(self, l): self.__light = l
   def get_light(self):
      if self.__light: return "1"
      return "0"

   def set_resident_proximity(self, p):  self.__resident_proximity = p
   def get_resident_proximity(self):
      if self.__resident_proximity: return "1"
      return "0"

   def set_intruder_proximity(self, p):  self.__intruder_proximity = p
   def get_intruder_proximity(self):
      if self.__intruder_proximity: return "1"
      return "0"

   def set_alarm_state(self, a): self.__alarm_state = a
   def get_alarm_state(self):
      if self.__alarm_state: return "1"
      return "0"

   def set_alarm_active_state(self, a): self.__alarm_active = a
   def get_alarm_active_state(self):
      if self.__alarm_active: return "1"
      return "0"

   def set_heater_state(self, h): self.__heater_state = h
   def get_heater_state(self):
      if self.__heater_state: return "1"
      return "0"

   def set_chiller_state(self, h): self.__chiller_state = h
   def get_chiller_state(self):
      if self.__chiller_state: return "1"
      return "0"

   def set_hvac_mode(self, h): self.__hvac_mode = h
   def get_hvac_mode(self):
      if self.__hvac_mode == HEATER: return "1"
      return "0"

   def set_dehumidifier(self, h): self.__dehumidifier = h
   def get_dehumidifier(self):
      if self.__dehumidifier: return "1"
      return "0"

   def get_state(self):
      '''
      Handle get state requests
      '''
      return "TR={0};HR={1};DS={2};LS={3};RP={4};IP={5};AS={6};AA={7};HES={8};CHS={9};HM={10};HUS={11};LOS={12}".format(self.get_temperature(),
                                                                                               self.get_humidity(),
                                                                                               self.get_door(),
                                                                                               self.get_light(),
                                                                                               self.get_resident_proximity(),
                                                                                               self.get_intruder_proximity(),
                                                                                               self.get_alarm_state(),
                                                                                               self.get_alarm_active_state(),
                                                                                               self.get_heater_state(),
                                                                                               self.get_chiller_state(),
                                                                                               self.get_hvac_mode(),
                                                                                               self.get_dehumidifier(),
                                                                                               self.get_lock())
house = HouseState()

class UserThread(threading.Thread):
   '''
   Thread to mimic user behavior
   '''

   def __init__(self):
      '''
      Set up the stop event
      '''
      super().__init__()
      self.__stop = threading.Event()

   def stop(self):
      '''
      Terminate this thread
      '''
      self.__stop.set()

   def run(self):
      '''
      Run the user simulation thread
      '''
      while not self.__stop.is_set():

         print("Current state: {}".format(house.get_state()))

         cmd = input("Enter a command: d=[toggle door], l=[toggle light], p=[toggle proximity], RET=[show current status]: ")

         if cmd == "d":  #TODO door shouldn't be able to open when locked
            if house.get_door() == "1": house.set_door(False)
            else: house.set_door(True)
            print ("door is now {}".format(house.get_door()))
         elif cmd == "l":
            if house.get_light() == "1": house.set_light(False)
            else: house.set_light(True)
            print ("light is now {}".format(house.get_light()))
         elif cmd == "p":
            if house.get_resident_proximity() == "1": house.set_resident_proximity(False)
            else: house.set_resident_proximity(True)
            print ("proximity is now {}".format(house.get_resident_proximity()))

      return

def main():
   '''
   Wait for incoming connections and run the simulation
   '''
   print('Starting House Simulator (Hub)')
   sys.stdout.flush()

   server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   server_address = (sys.argv[1], int(sys.argv[2]))
   server.bind(server_address)
   server.listen(1)  # max backlog of connections


   while True:

      print ('Waiting for house connection on {0}:{1}'.format(server_address[0], server_address[1]))
      sys.stdout.flush()
      connection, client_address = server.accept()
      print("Connection from {0}".format(client_address))

      user_thread = UserThread()
      user_thread.start()
     
      try:
         while True:
            data = connection.recv(1024).decode('ascii')

            if data:
               if data[:2] == "GS":
                  su = "SU:{}.\n".format(house.get_state())
                  connection.sendall(su.encode())

               elif data[:2] == "SS":
                  house.set_state(data)
                  connection.sendall("OK.\n".encode())

               else:
                  print ("Error, unknown request: {}".format(data))

            else: break

            house.update_simulation()

      except Exception as e:
         print("Error: %s" % str(e))
         traceback.print_exc()
      finally:
         print("closing!")
         connection.close()
         user_thread.stop()
   return

if __name__ == '__main__':
   main()
