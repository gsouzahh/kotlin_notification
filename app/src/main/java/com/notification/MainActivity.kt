package com.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        /* Para reabrir o app quando click na notificação pela barra de tarefas */

        val intent = Intent(this, TesteFragment::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Primeiro cria-se o channel notification, é nele q é postado as notificações
        //  - Definir um channelID(único) e um channelName
        //    * channelID é pra diferenciar entre vários tipos de channel notification no app

        // Cria-se uma função para configurar o notificationChannel

        createNotificationChannel()

        // Proxima etapa é criar os detalhes da notificação

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Awesome notification")        // título da notificação
            .setContentText("This is the content text")     // descrição...
            .setSmallIcon(R.drawable.ic_bike)               // icon...
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // prioridade...
            .setContentIntent(pendingIntent)
            .build()

        // Agora é criado mais um gerenciador só q um pouco mais diferente (compat)

        val notificationManager = NotificationManagerCompat.from(this)

        /*  Agora basta juntar o notificationManagerCompat e mostrar a notificação
         *  criada em uma ação de button por exemplo */

        binding.btnNotification.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }


        setContentView(binding.root)
    }

    fun createNotificationChannel() {
        // Verificar se é antes do android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //Build.VERSION_CODES.O = Android 8
        {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                    // Os parâmetros da criação de um notification channel é o id, nome e a importância dessa notificação
                    // Oo default pra baixo a notificação não terá efeitos sonoros, posteriores sim.
                    .apply {
                        lightColor = Color.RED
                        enableLights(true)
                    }
            /* Após configurar o Notification Channel, cria-se um Notification Manager, ele que vai de fato criar o
             * Notification Channel que é criado ai em cima */

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel) // passamos nosso channel criado
        }
    }


}