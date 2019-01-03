from NetworkTransfer.TCPServer import TCPServer
from IKControl.CommandParser import CommandParser

command_parser = CommandParser()
tcpServer = TCPServer(command_parser)
tcpServer.create_server()