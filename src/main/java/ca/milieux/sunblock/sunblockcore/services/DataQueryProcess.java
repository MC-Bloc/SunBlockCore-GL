package ca.milieux.sunblock.sunblockcore.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.lang.Process;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;


public class DataQueryProcess {


    static String CPUTempPath = "/sys/class/thermal/thermal_zone1/temp";
    static String SunblockDataPath = "/home/pc/SunblockData/solar_data.json";
    static float MAXBATTERYCAPACITY = 1200f; // max battery capacity in Watts
    static int MAXMEMORY = 10; // Last 10 seconds

    // array of the past 10 power consumption values to
    static Queue<Float> powerConsumptionHistory = new LinkedList<>();

    public static float GetCPUTemp() {
        try {
            ProcessBuilder pb = new ProcessBuilder("cat", CPUTempPath);

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String data = reader.readLine();
            float temp = (float)Integer.parseInt(data);
            temp /= 1000;

            int exitCode = process.waitFor();
            return temp;
        }

        catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String GetTimeRemaining() {
        float battRemaining = GetServerData(SolarDataTypes.BATTREMAINING);

        float loadPower = GetServerData(SolarDataTypes.LPOWER);

        if (powerConsumptionHistory.size() < MAXMEMORY) {
            powerConsumptionHistory.add(loadPower);
            return "Calculating...";
        } else {
            powerConsumptionHistory.remove();
            powerConsumptionHistory.add(loadPower);

            float avgPowerConsumption = SumOfQueue(powerConsumptionHistory) / powerConsumptionHistory.size();
            float timeRemaining = (battRemaining / 100 ) * (MAXBATTERYCAPACITY / avgPowerConsumption);
            // Truncate to 2 decimal places
            return Double.toString(Math.floor(timeRemaining * 100) / 100);
        }
    }

    public static String GetTimestamp() {
        int count_lines = 2 + 12; //2 for the brackets, 11 for the number of entries.

        try {
            ProcessBuilder pb = new ProcessBuilder("cat", SunblockDataPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (int i = 0; i < count_lines; i++) {
                String data = reader.readLine();
                if (data != null) {
                    data = data.strip();
                    if (data.contains("Timestamp")){
                        String[] timeParts = data.strip().split(" ");
                        String timestamp = timeParts[timeParts.length - 1];
                        timeParts = timestamp.split(":");
                        if (timeParts.length > 1) {
                            return timeParts[0] + ":" + timeParts[1];
                        } else {
                            return "00:00";
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("ERROR: There was an error calling GetTimestamp: " + e.getMessage() );
            return null;
        }
    }

    public static float GetServerData(SolarDataTypes property) {
        if (property == SolarDataTypes.TIMESTAMP) {
            return -1f;
        }

        int count_lines = 2 + 12; //2 for the brackets, 11 for the number of entries.

        try {
            ProcessBuilder pb = new ProcessBuilder("cat", SunblockDataPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (int i = 0; i < count_lines; i++) {
                String data = reader.readLine();

                if (data != null) {
                    data = data.strip();
                    if (property == SolarDataTypes.PVVOLTAGE && data.contains("PVVoltage")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.PVCURRENT && data.contains("PVCurrent")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.PVPOWER && data.contains("PVPower")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.BATTVOLTAGE && data.contains("BattVoltage")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.BATTCHARGECURRENT && data.contains("BattChargeCurrent")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.BATTCHARGEPOWER && data.contains("BattChargePower")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.LPOWER && data.contains("LoadPower")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.BATTREMAINING && data.contains("BattPercentage")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.BATTOVERALLCURRENT && data.contains("BattOverallCurrent")){
                        return GetValue(data);
                    } else if (property == SolarDataTypes.SYSTEMPOWERDRAW && data.contains("CPUPowerDraw")){
                        return GetValue(data);
                    }
                }
            }
            return 7.7f;
        } catch (Exception e) {
            System.err.println("SunBlockCore ERROR: Failed to complete DataQueryProcess::GetServerData. \nReason:  " + e.getMessage());
            return -1f;
        }
    }

    static float GetValue(String data) {
        // Extract the float value from the string entry from the JSON file

        float ret_val = 0.0f;
        String[] split_data = data.split(":", 2);

        if (split_data.length > 1 && split_data[1] != null ) {
            ret_val = Float.valueOf(split_data[1].replace('"', '\0').replace(',', '\0').strip());
        }

        return ret_val;

    }

    static float SumOfQueue(Queue q){
        float _sum = 0f;
        Object[] arr = q.toArray();

        for (int i = 0; i < q.size(); i++) {
            _sum += Float.parseFloat(arr[i].toString());
        }

        return _sum;
    }

    public static String PowerProfile(){
        int count_lines = 2 + 12; //2 for the brackets, 11 for the number of entries.

        try {
            ProcessBuilder pb = new ProcessBuilder("cat", SunblockDataPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (int i = 0; i < count_lines; i++) {
                String data = reader.readLine();
                if (data != null) {
                    data = data.strip();
                    if (data.contains("PowerProfile")){
                        return data.split(":")[1].replace("\"", "").replace("-", " ");
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("ERROR: There was an error calling PowerProfile: " + e.getMessage() );
            return null;
        }
    }

}
