package SmartFilePackage;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main implements InvocationHandler{
    Object target;
    Path path;
    BufferedWriter bf;
    public Main(Object o, String name) throws Exception{
        this.target = o;
        path = Paths.get("Logs/"+ name + "/logger.txt");
        if(Files.exists(path)){
            bf = new BufferedWriter(new FileWriter("Logs/"+ name +"/logger.txt"));
        }else{
            path = Paths.get("Logs/" + name + "/");
            Files.createDirectories(path);
            path = path.resolve("logger.txt");
            Files.createFile(path);
            bf = new BufferedWriter(new FileWriter("Logs/"+ name +"/logger.txt"));
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder data = new StringBuilder(
            "Method : " + method.getName() +
            " | Declaring Class : " + method.getDeclaringClass()
        );
        
        method.invoke(target, args);
        
        if(args != null){
            data.append(" | Args : " + Arrays.stream(args).toList() + "\n");
        }else{
            data.append(" | Args : null \n");
        }
        
        bf.write(data.toString());

        return null;
    }

    public void close() throws IOException{
        ((LocalFileManager) target).close();
        bf.close();
    }

    public static void displayBanner() {
        System.out.println("""

            ____                  _             _ 
            |_   _|__ _ __ _ __ ___ (_)_ __   __ _| |
            | |/ _ \\ '__| '_ ` _ \\| | '_ \\ / _` | |
            | |  __/ |  | | | | | | | | | | (_| | |
            |_|\\___|_|  |_| |_| |_|_|_| |_|\\__,_|_|

            :: Terminal Based Writer ::             (v1.0.0)

            --------------------------------------------------
            Starting Terminal Based Writer...
            --------------------------------------------------
        """);
    }


    public static void main(String[] args) throws Exception{
        displayBanner();
        System.out.print("File Name : ");
        Scanner sc = new Scanner(System.in);
        String fileString = sc.nextLine();
        System.out.print("Logger Name : ");
        String loggerString = sc.nextLine();
        LocalFileManager mFileManager = new LocalFileManager(fileString, sc);
        Main handler = new Main(mFileManager, loggerString);

        Object proxyObject = Proxy.newProxyInstance(
            Main.class.getClassLoader(),
            new Class<?>[]{
                SmartFileReader.class, SmartFileWriter.class, SmartFileDeleter.class
            },
            handler
        );
        
        SmartFileWriter smflwr = (SmartFileWriter) proxyObject;
        SmartFileReader smflrd = (SmartFileReader) proxyObject;
        SmartFileDeleter smfldel = (SmartFileDeleter) proxyObject;
        
        System.out.println("");

        System.out.println("1) Read\n2) Write\n3) Delete\n0) Exit");
        int choice = -1;

        while (choice != 0 && choice != 3) {
            System.out.print("Give the choice : ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 0:
                    break;
                case 1:
                    smflrd.completeFileReader();
                    break;
                case 2:
                    System.out.println("Enter text {:quit to finish}:- ");
                    smflwr.FileWriter(fileString);
                    break;
                case 3:
                    smfldel.FileDeleter(fileString);
                    break;
                default:
                    System.out.println("Please give a valid choice");
            }
        }

        sc.close();
        handler.close();
    }
}
