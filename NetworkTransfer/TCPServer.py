import socket
import threading


class TCPServer:
    def __init__(self, parser):
        self.bind_ip = '0.0.0.0'
        self.bind_port = 9999
        self.buf_map = {}
        self.command_parser = parser

    def get_remain_buf(self, client_socket):
        if client_socket not in self.buf_map:
            self.buf_map[client_socket] = ""
        return self.buf_map[client_socket]

    def handle_client_connection(self, client_socket):
        is_connection_alive = True
        while is_connection_alive:
            request_line = client_socket.recv(1024)
            remain_buf = self.get_remain_buf(client_socket)
            remain_buf = remain_buf + request_line

            # search for line_breaks
            line_break_idx = remain_buf.find("\n")

            while line_break_idx != -1:
                command = remain_buf[0:line_break_idx]
                remain_buf = remain_buf[line_break_idx + 1:]
                command = command.rstrip()
                if len(command) > 0:
                    if command.lower() == "bye" or command.lower() == "quit":
                        is_connection_alive = False
                    else:
                        execute_result = self.handle_client_command(command)
                        client_socket.send(execute_result)
                line_break_idx = remain_buf.find("\n")
            self.buf_map[client_socket] = remain_buf

        client_socket.close()
        del self.buf_map[client_socket]
        print "Connection broken, client thread quit!"

    def handle_client_command(self, command_line):
        result = self.command_parser.dispatch(command_line)
        print "Got command:%s result:%s" % (command_line, result)
        return result + "\n"

    def create_server(self):
        print "Creating socket"
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1) 
        print "Binding to %s:%d" % (self.bind_ip, self.bind_port)
        self.server.bind((self.bind_ip, self.bind_port))
        print "Listening"
        self.server.listen(5)  # max backlog of connections

        while True:
            client_sock, address = self.server.accept()
            print 'Accepted connection from {}:{}'.format(address[0], address[1])
            client_handler = threading.Thread(
                target=self.handle_client_connection,
                args=(client_sock,)
            )
            client_handler.start()
