package services;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Created by vacax on 07/06/17.
 */
public class BootStrapServices {

    private static BootStrapServices instancia;

    private BootStrapServices(){

    }

    public static BootStrapServices getInstance(){
        if(instancia == null){
            instancia=new BootStrapServices();
        }
        return instancia;
    }

    private void startDb() {
        try {
            Server.createTcpServer(
                    "-tcpAllowOthers",
                    "-tcpDaemon").start();
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    public void init(){
        startDb();
    }
}
