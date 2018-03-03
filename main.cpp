#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <vector>


#define SERVER_PORT 3490
#define QUEUE_SIZE 8
#define BUFOR_SIZE 100

pthread_mutex_t example_mutex;
pthread_cond_t count_threshold_cv;

pthread_mutex_t bufer1_mutex;
pthread_cond_t game_cond;
int count=0;
std::vector<int> clients;
using namespace std;




//struktura zawierająca dane, które zostaną przekazane do wątku
struct thread_data_t
{
    int connect;
};

//funkcja opisującą zachowanie wątku - musi przyjmować argument typu (void *) i zwracać (void *)
void *ThreadBehavior(void *t_data)
{
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t*)t_data;

    pthread_mutex_lock(&example_mutex);
    count++;
    int buffer = 0;
    read((*th_data).connect, &buffer, sizeof(buffer));


    if(count%4==0){
        pthread_cond_signal(&count_threshold_cv);
	pthread_cond_signal(&count_threshold_cv);
	pthread_cond_signal(&count_threshold_cv);

    }
    else{
        pthread_cond_wait(&count_threshold_cv, &example_mutex);

    }
    char buf[BUFOR_SIZE];
    sprintf(buf,"%d\n", 1002);

    write((*th_data).connect, buf, strlen(buf));
    clients.push_back((*th_data).connect);

    fprintf(stdout, "Klient o ID %d nawiazal polaczenie\n", clients.at(clients.size() - 1));

    pthread_mutex_unlock(&example_mutex);


    pthread_mutex_init(&bufer1_mutex, NULL);
    pthread_cond_init (&game_cond, NULL);

    while(1) {

//////////////////////////////////////////////////////////////////////////////////
        for (unsigned int i = 0; i < clients.size() ; i+=4) {
            if (clients.at(i) == (*th_data).connect) {
		int strzal1;
		int strzal2;

		int propozycja1;
		int propozycja2;
			
		pthread_mutex_lock(&bufer1_mutex);

		int buffer1 = 0;

		pthread_mutex_unlock(&bufer1_mutex);

		while(1) {
			//PIERWSZY
		        sprintf(buf, "%d\n", 1003);
		        ///pozwol strzelac
		        write((*th_data).connect, buf, strlen(buf));
		        fprintf(stdout, "Klient o ID %d moze strzelac\n", clients.at(i));

		        ///odczyt strzalu
		        read((*th_data).connect, &buffer1, sizeof(buffer1));
			strzal1 = buffer1;
			if(buffer1==1005) {
				fprintf(stdout, "Klient %d odnosi zwyciestwo!!!\n", clients.at(i+1));
				break;
			} else {
		            	fprintf(stdout, "Klient %d wykonal strzal w pole: %d\n", clients.at(i), buffer1);
			}
			propozycja1 = strzal1 + 10000;
		        sprintf(buf, "%d\n", propozycja1);
			write(clients.at(i+2), &buf, strlen(buf));


			//DRUGI
		        sprintf(buf, "%d\n", 1003);
		        ///pozwol strzelac
		        write(clients.at(i+2), buf, strlen(buf));
		        fprintf(stdout, "Klient o ID %d moze strzelac\n", clients.at(i+2));

		        ///odczyt strzalu
		        read(clients.at(i+2), &buffer1, sizeof(buffer1));
			strzal2 = buffer1;
			if(buffer1==1005) {
				fprintf(stdout, "Klient %d odnosi zwyciestwo!!!\n", clients.at(i+1));
				break;
			} else {
		            	fprintf(stdout, "Klient %d wykonal strzal w pole: %d\n", clients.at(i+2), buffer1);
			}
			propozycja2 = strzal2 + 10000;
		        sprintf(buf, "%d\n", propozycja2);
			write(clients.at(i), &buf, strlen(buf));

			if (strzal1 == strzal2) {
				break;
			}
		}

////////////////////////////////////////////////////////////////////////////////////////////////

                sprintf(buf, "%d\n", buffer1);
                ///wyslij wspolrzedne do przeciwnika
                write(clients.at(i+1), buf, strlen(buf));
		write(clients.at(i+3), buf, strlen(buf));
                if(buffer1==1005) {
                    pthread_exit(NULL);
		}

                ///czy trafiles
                read(clients.at(i+1), &buffer1, sizeof(buffer1));
		read(clients.at(i+3), &buffer1, sizeof(buffer1));
	
		if(buffer1==101||buffer1==102) {
                    	fprintf(stdout, "Klient %d spudlowal\n", clients.at(i));
		}else if(buffer1==103||buffer1==104) {
			fprintf(stdout, "Klient %d trafil w czesc statku\n", clients.at(i));
		}else {
			fprintf(stdout, "Klient %d zatopil statek\n", clients.at(i));
		}

                sprintf(buf, "%d\n", buffer1);
                ///czy trafiles
                write(clients.at(i), buf, strlen(buf));
		write(clients.at(i+2), buf, strlen(buf));
                if(buffer1==1005 || buffer1==0) {
                	pthread_exit(NULL);
		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		while (1) {       
			//PIERWSZY         
			sprintf(buf, "%d\n", 1003);
		        ///pozwol strzelac
		        write(clients.at(i+1), buf, strlen(buf));
		        if(buffer1==1005) {
		            pthread_exit(NULL);
			}
		        fprintf(stdout, "Klient o ID %d moze strzelac\n", clients.at(i+1));

		        ///odczyt strzalu
		        read(clients.at(i+1), &buffer1, sizeof(buffer1));
			strzal1 = buffer1;
			if(buffer1==1005) {
				fprintf(stdout, "Klient %d oraz %d odnosi zwyciestwo!!!\n", clients.at(i), clients.at(i+3));
				break;
			} else {
		            	fprintf(stdout, "Klient %d wykonal strzal w pole: %d\n", clients.at(i+1), buffer1);
			}
			propozycja1 = strzal1 + 10000;
		        sprintf(buf, "%d\n", propozycja1);
			write(clients.at(i+3), &buf, strlen(buf));

			//DRUGI
		        sprintf(buf, "%d\n", 1003);
		        ///pozwol strzelac
		        write(clients.at(i+3), buf, strlen(buf));
		        fprintf(stdout, "Klient o ID %d moze strzelac\n", clients.at(i+3));

		        ///odczyt strzalu
		        read(clients.at(i+3), &buffer1, sizeof(buffer1));
			strzal2 = buffer1;
			if(buffer1==1005) {
				fprintf(stdout, "Klient %d oraz %d odnosi zwyciestwo!!!\n", clients.at(i), clients.at(i+3));
				break;
			} else {
		            	fprintf(stdout, "Klient %d wykonal strzal w pole: %d\n", clients.at(i+3), buffer1);
			}
			propozycja2 = strzal2 + 10000;
		        sprintf(buf, "%d\n", propozycja2);
			write(clients.at(i+1), &buf, strlen(buf));

			if (strzal1 == strzal2) {
				break;
			}
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                sprintf(buf, "%d\n", buffer1);
                ///wyslij wspolrzedne do przeciwnika
                write(clients.at(i), buf, strlen(buf));
                write(clients.at(i+2), buf, strlen(buf));
                if(buffer1==1005) {
                    pthread_exit(NULL);
		}
                ///czy trafiles
                read(clients.at(i), &buffer1, sizeof(buffer1));
                read(clients.at(i+2), &buffer1, sizeof(buffer1));
                if(buffer1==1005 || buffer1==0) {
                    pthread_exit(NULL);
		}
                if(buffer1==101||buffer1==102) {
                    	fprintf(stdout, "Klient %d spudlowal\n", clients.at(i+1));
		}else if(buffer1==103||buffer1==104) {
			fprintf(stdout, "Klient %d trafil w czesc statku\n", clients.at(i+1));
		}else {
			fprintf(stdout, "Klient %d zatopil statek\n", clients.at(i+1));
		}

                sprintf(buf, "%d\n", buffer1);
                ///czy trafiles
                write(clients.at(i+1), buf, strlen(buf));
                write(clients.at(i+3), buf, strlen(buf));
                if(buffer1==1005) {
                    pthread_exit(NULL);
		}
            }
        }
}
    pthread_exit(NULL);
}

//funkcja obsługująca połączenie z nowym klientem
void handleConnection(int connection_socket_descriptor) {
    //wynik funkcji tworzącej wątek
    int create_result = 0;
    //uchwyt na wątek
    pthread_t thread1;

    struct thread_data_t *t_data = static_cast<thread_data_t *>(malloc(sizeof(struct thread_data_t)));
    t_data->connect=connection_socket_descriptor;

    create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *)t_data);
    if (create_result){
        exit(-1);
    }
}

int main(int argc, char* argv[])
{
    int server_socket_descriptor;
    int connection_socket_descriptor;
    int bind_result;
    int listen_result;
    char reuse_addr_val = 1;
    struct sockaddr_in server_address;

    //inicjalizacja gniazda serwera

    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(SERVER_PORT);
     pthread_mutex_init(&example_mutex, NULL);
     pthread_cond_init (&count_threshold_cv, NULL);

    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);

    if (server_socket_descriptor < 0)
    {
        fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }
    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse_addr_val, sizeof(reuse_addr_val));

    bind_result = bind(server_socket_descriptor, (struct sockaddr*)&server_address, sizeof(struct sockaddr));
    if (bind_result < 0)
    {
        fprintf(stderr, "%s: Błąd przy próbie dowiązania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);

    }

    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "%s: Błąd przy próbie ustawienia wielkości kolejki.\n", argv[0]);
        exit(1);
    }

    while(1)
    {
        connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
        if (connection_socket_descriptor < 0)
        {
            fprintf(stderr, "%s: Błąd przy próbie utworzenia gniazda dla połączenia.\n", argv[0]);
            exit(1);
        }

        handleConnection(connection_socket_descriptor);

    }

    close(server_socket_descriptor);
    return(0);
}
