#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <stdbool.h>
#include <signal.h>
#include <pthread.h>
#include <stdint.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define MAXWORDS 10000
#define MAXLEN 1000
#define MAXCLIENTS 100

char usernames[MAXCLIENTS][MAXLEN];
int connfds[MAXCLIENTS]; //to write to each client
int numClients = 0;
#define SIZE 1024

void *server_child(void *ptr) {
	int clientIndex = (int)(intptr_t) ptr;
	//create dedicated server fifo - use pthread_self() instead of getpid() to create unique server fifo
	//send it to the client - clientfps[clientIndex]
	//wait to read from client in a loop
	//process the command
				//create and send new server fifo to the client
				//for private one-on-one communcations						
				char message[SIZE];
				char clientLine[SIZE];
				sprintf(message, "====== Welcome to the Message App ======\n\tPlease Enter a Name\n");
				write(connfds[clientIndex], message, strlen(message));
				read(connfds[clientIndex], &clientLine, SIZE);
				char* found;
				found  = strtok(clientLine, "\n");
				strcpy(usernames[clientIndex], found);
				int n;
				while(n = read(connfds[clientIndex], &clientLine, SIZE)){
					clientLine[n] = '\0';
					if(strncmp(clientLine, "list", 4) == 0){
						//memset(clientLine, 0, sizeof(clientLine));
						for(int i = 0; i < numClients; i++)
						{
					//	fprintf(clientfps[clientIndex], "user\n");
						//fflush(clientfps[clientIndex]);
						//fflush(clientfps[clientIndex]);
					/*	message = "--";
						strcat(message, usernames[i]);
						strcat(message, "\n");*/
						if(strcmp(usernames[i],"\0") == 0)
							continue;
						sprintf(message, "--> %s\n", usernames[i]);
						write(connfds[clientIndex], message, strlen(message));
						}
					}
					else if(strncmp(clientLine, "broadcast", 9) == 0){
						found = strstr(clientLine, "broadcast");
						found += 9;
						while(*found == ' ')
							found++;
						found = strtok(found, "\n");
						for(int i = 0; i < numClients; i++){
							if(i == clientIndex)
								continue;
							sprintf(message, "Message from %s: %s\n", usernames[clientIndex], found);
							write(connfds[i], message, strlen(message));
						}
						sprintf(message, "Message Sent!\n");
						write(connfds[clientIndex], message, strlen(message));
							
					}
					else if(strncmp(clientLine, "random", 6) == 0){
						found = strstr(clientLine, "random");
						found += 6;

						while(*found == ' ')
							found++;
						found = strtok(found, "\n");
						int num;
						do{
						num = rand() % ((numClients-1) + 1);
						}while(num == clientIndex);
						sprintf(message, "Message from %s: %s\n", usernames[clientIndex], found);
						write(connfds[num], message, strlen(message));
						sprintf(message, "Message sent to %s\n", usernames[num]);
						write(connfds[clientIndex], message, strlen(message));
					}
					else if(strncmp(clientLine, "close", 5) == 0){
						break;
					}
					else 
					{
						 found = strstr(clientLine, "send");
						if(found){
									int num = -1;
									found +=4;

									while(*found == ' ')
										found++;
									char *word = strtok(found, " \n");
									for(int i = 0; i < numClients; i++)
									{
										if(strcmp(usernames[i], word) == 0){
											num = i;
											break;
										}										
									//	found++;
									}
									if(num == -1 || num == clientIndex){
										sprintf(message, "User is not available to send to\n");
										write(connfds[clientIndex], message, strlen(message));
									}
									else{
								//	while(*found == ' ')
								//		found++;

									char *strt = strtok(NULL, "\n");
									if(strt && *strt != '\0'){
									
																
								sprintf(message, "Message from %s!: %s\n", usernames[clientIndex], strt);
									write(connfds[num], message, strlen(message));
										//it should be added here
										sprintf(message, "Message Sent to %s!\n", word);
										write(connfds[clientIndex], message, strlen(message));
									}
									else
									{
										sprintf(message, "No message send to user\n");
										write(connfds[clientIndex], message, strlen(message));
									}
								}
							}
							else{
								sprintf(message, "Cannot recognize that command\n");
								write(connfds[clientIndex], message, strlen(message));
							}
						}
						
					}
				//exit(0); //this has to change, right?

				printf("disconnected client %d\n", clientIndex+1);
				sprintf(message, "Thanks for using my App!\n");
				write(connfds[clientIndex], message, strlen(message));
				sprintf(usernames[clientIndex], "\0");
			 	close(connfds[clientIndex]);
				pthread_exit(NULL);
				return NULL;
}


void pexit(char word[]){
	printf("%s\n", word);
	exit(0);
}

int main() {
	int listenfd = 0;
	struct sockaddr_in serv_addr;

	char buffer[1025];
//	time_t ticks;

	if((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		pexit("socket() error");
	
	memset(&serv_addr, '0', sizeof(serv_addr));
	memset(buffer, '0', sizeof(buffer));

	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

	int port = 4999;
	do{
		port++;
		serv_addr.sin_port = htons(port);
	}
	while(bind(listenfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) < 0);
	printf("bind() succeeded for port #%d\n", port);

	if(listen(listenfd, 10) < 0)
		pexit("listen() error.");
	srand(getpid() + time(NULL) + getuid());
	while (1) {
		connfds[numClients] = accept(listenfd, (struct sockaddr*)NULL, NULL);
		printf("connected to client %d.\n", numClients+1);
		numClients++;

		pthread_t thread1;
		pthread_create(&thread1, NULL, server_child, (void *)(intptr_t)numClients-1);

//		printf("client %d just disconnected\n", numClients);
		}
		
}

