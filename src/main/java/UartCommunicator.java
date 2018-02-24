import com.pi4j.io.serial.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.IOException;
import java.rmi.server.ExportException;

/**
 * Created by User on 2018-02-24.
 */
public class UartCommunicator {

    public  UartCommunicator(){

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        console = new Console();

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();

        serial.addListener(new SerialDataEventListener() {

            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.

                // print out the data received to the console
                try {
                    console.println("[HEX DATA]   " + event.getHexByteString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // create serial config object
        SerialConfig config = new SerialConfig();

        try{
            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            config.device(SerialPort.getDefaultPort())
                    .device("/dev/ttyUSB0")
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._2)
                    .flowControl(FlowControl.NONE);


            // display connection details
            console.box(" Connecting to: " + config.toString(),
                    " We are sending ASCII data on the serial port every 1 second.",
                    " Data received on serial port will be displayed below.");


            // open the default serial device/port with the configuration settings
            serial.open(config);
        }
        catch(IOException ex) {
            console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
        }
        catch (InterruptedException e){
            console.println("InterruptedException !!!");
            e.getMessage();
            e.printStackTrace();
        }
    }

    void sendData() {
        if(console.isRunning()){
            try {
                // write a individual bytes to the serial transmit buffer

                //Sync
                serial.write((byte) 0x7F, (byte) 0x90, (byte) 0x01, (byte) 0x11, (byte) 0x26, (byte) 0x03);

//                serial.write((byte) 0x7F);
//                serial.write((byte) 0x90);
//                serial.write((byte) 0x01);
//                serial.write((byte) 0x11);
//                serial.write((byte) 0x26);
//                serial.write((byte) 0x03);

                //Reset
                serial.write((byte) 0x7F, (byte) 0x10, (byte) 0x01,(byte) 0x01, (byte) 0x46, (byte) 0x09);

                //                serial.write();
//                serial.write();
//                serial.write(;
//                serial.write();
//                serial.write();

//                serial.write((byte) 0x7F);
//                serial.write((byte) 0x10);
//                serial.write((byte) 0x01);
//                serial.write((byte) 0x01);
//                serial.write((byte) 0x46);
//                serial.write((byte) 0x09);


                // write a simple string to the serial transmit buffer
                //serial.write("Second Line");

                // write a individual characters to the serial transmit buffer
                //serial.write('\r');
                //serial.write('\n');

                // write a string terminating with CR+LF to the serial transmit buffer
                //serial.writeln("Third Line");
            }
            catch(IllegalStateException ex){
                ex.printStackTrace();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }else {} //TODO - react for not running console
    }

    private final Console console;
    private final Serial serial;
}
