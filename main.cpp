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
#define QUEUE_SIZE 5
#define BUF_SIZE 100
pthread_mutex_t example_mutex;
 pthread_cond_t count_threshold_cv;

pthread_mutex_t game_mutex;
pthread_cond_t game_cond;
int count=0;
std::vector<int> arr;
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
  //  fprintf(stdout,"%d\n",(*th_data).connect);


    pthread_mutex_lock(&example_mutex);
    count++;
    int buffer = 0;
    read((*th_data).connect, &buffer, sizeof(buffer));

    if(count%2==1){
        pthread_cond_wait(&count_threshold_cv, &example_mutex);

    }
    if(count%2==0){
        pthread_cond_signal(&count_threshold_cv);

    }
 //   fprintf(stdout,"%d\n",buffer);
    char buf2[100];
    sprintf(buf2,"%d\n", 1002);

    write((*th_data).connect, buf2, strlen(buf2));
    arr.push_back((*th_data).connect);

    for (unsigned int i = 0; i < arr.size(); ++i) {
        fprintf(stdout, "%d\n", arr.at(i));
    }


    pthread_mutex_unlock(&example_mutex);

//    fprintf(stdout,"poszlo");


    pthread_mutex_init(&game_mutex, NULL);
    pthread_cond_init (&game_cond, NULL);
    pthread_mutex_lock(&game_mutex);
    //pthread_cond_wait(&game_cond, &game_mutex);

    while(1) {
        for (unsigned int i = 0; i < arr.size() ; i+=2) {
            if (arr.at(i) == (*th_data).connect) {
                if (arr.at(i) == (*th_data).connect) {
                    char buf4[100];
                    sprintf(buf4, "%d\n", 1003);
                    ///pozwol strzelac
                    write((*th_data).connect, buf4, strlen(buf4));
                    fprintf(stdout, "%d\n", 1003);

                }

                int buffer1 = 0;

                if (arr.at(i) == (*th_data).connect) {

                    ///odczyt strzalu
                    read((*th_data).connect, &buffer1, sizeof(buffer1));
                    fprintf(stdout, "%d\n", buffer1);

                    pthread_cond_signal(&game_cond);
                }

                if (arr.at(i+1) == (*th_data).connect) {
                    pthread_cond_wait(&game_cond, &game_mutex);
                }

                pthread_mutex_unlock(&game_mutex);


                if (arr.at(i) == (*th_data).connect) {

                    char buf3[100];
                    sprintf(buf3, "%d\n", buffer1);
                    ///wyslij wspolrzedne do przeciwnika
                    write(arr.at(i+1), buf3, strlen(buf3));
                    if(buffer1==1005)
                        pthread_exit(NULL);

                    fprintf(stdout, "%d\n", buffer1);
                    ///czy trafiles
                    read(arr.at(i+1), &buffer1, sizeof(buffer1));

                    fprintf(stdout, "%d\n", buffer1);


                    char buf4[100];
                    sprintf(buf4, "%d\n", buffer1);
                    ///czy trafiles
                    write(arr.at(i), buf4, strlen(buf4));
                    if(buffer1==1005 || buffer1==0)
                        pthread_exit(NULL);


                    fprintf(stdout, "%d\n", buffer1);

                    char buf5[100];
                    sprintf(buf5, "%d\n", 1003);
                    ///pozwol strzelac
                    write(arr.at(i+1), buf5, strlen(buf5));
                    if(buffer1==1005)
                        pthread_exit(NULL);
                    fprintf(stdout, "%d\n", 1003);

                    ///odczyt strzalu
                    read(arr.at(i+1), &buffer1, sizeof(buffer1));
                    if(buffer1==1005)
                        pthread_exit(NULL);
                    fprintf(stdout, "%d\n", buffer1);

                    char buf6[100];
                    sprintf(buf6, "%d\n", buffer1);
                    ///wyslij wspolrzedne do przeciwnika
                    write(arr.at(i), buf6, strlen(buf6));
                    if(buffer1==1005)
                        pthread_exit(NULL);
                    fprintf(stdout, "%d\n", buffer1);
                    ///czy trafiles
                    read(arr.at(i), &buffer1, sizeof(buffer1));
                    if(buffer1==1005 || buffer1==0)
                        pthread_exit(NULL);
                    fprintf(stdout, "%d\n", buffer1);


                    char buf7[100];
                    sprintf(buf7, "%d\n", buffer1);
                    ///czy trafiles
                    write(arr.at(i+1), buf7, strlen(buf7));
                    if(buffer1==1005)
                        pthread_exit(NULL);

                    fprintf(stdout, "%d\n", buffer1);

                }
            }
        }
}





    //   while((odp=read((*th_data).connect, bufor, BUF_SIZE))>0)
 //       write(1,bufor,odp);
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

//	scanf("%s",a);



//       pthread_cond_signal(&count_threshold_cv);
    create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *)t_data);
    //fprintf(stdout,"cos");
    if (create_result){
        //   fprintf("Błąd przy próbie utworzenia wątku, kod błędu: %d\n", create_result);
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

    //    pthread_mutex_lock(&example_mutex);
        handleConnection(connection_socket_descriptor);
   //     	  pthread_mutex_unlock(&example_mutex);
//	close(connection_socket_descriptor);

    }

    close(server_socket_descriptor);
    return(0);
}
