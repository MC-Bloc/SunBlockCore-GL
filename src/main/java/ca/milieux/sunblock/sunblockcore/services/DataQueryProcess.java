package ca.milieux.sunblock.sunblockcore.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.lang.Process;
import java.util.LinkedList;
import java.util.Queue;


public class DataQueryProcess {
    
    enum SOLAR_DATA {
        TIMESTAMP, 
        PVVOLTAGE,
        PVCURRENT,
        PVPOWER,
        BATTVOLTAGE,
        BATTCHARGECURRENT,
        BATTCHARGEPOWER,
        LPOWER,
        BATTREMAINING,
        BATTOVERALLCURRENT,
        SYSTEMPOWERDRAW
    }
    static String CPUTempPath = "/sys/class/thermal/thermal_zone1/temp";
    static String SunblockDataPath = "/home/pc/SunblockData/solar_data.json";
    static float MAXBATTERYCAPACITY = 600f; // max battery capacity in Watts
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

    public static float GetSysPower() {
        return GetServerData(SOLAR_DATA.SYSTEMPOWERDRAW);
    }
    
    public static float GetPVVoltage() {
        return GetServerData(SOLAR_DATA.PVVOLTAGE);
    }

    public static float GetPVCurrent() {
        return GetServerData(SOLAR_DATA.PVCURRENT);
    }

    public static float GetPVPower() {
        return GetServerData(SOLAR_DATA.PVPOWER);
    }

    public static float GetBattVoltage() {
        return GetServerData(SOLAR_DATA.BATTVOLTAGE);
    }

    public static float GetBattChargeCurrent() {
        return GetServerData(SOLAR_DATA.BATTCHARGECURRENT);
    }

    public static float GetBattChargePower() {
        return GetServerData(SOLAR_DATA.BATTCHARGEPOWER);
    }

    public static float GetLPower() {
        return GetServerData(SOLAR_DATA.LPOWER);
    }

    public static float GetBattRemaining() {
        return GetServerData(SOLAR_DATA.BATTREMAINING);
    }

    public static float GetBattOverallCurrent() {
        return GetServerData(SOLAR_DATA.BATTOVERALLCURRENT);
    }

    public static String GetTimeRemaining() {
        float battRemaining = GetBattRemaining();

        float loadPower = GetLPower();

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

    // UNTESTED
    public static String GetTimestamp() { 

        String path = "/home/pc/SunblockData/solar_data.json";
        int count_lines = 2; // only need to read until the 2nd line 

        try {
            ProcessBuilder pb = new ProcessBuilder("cat", path);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String data = "";
            for (int i = 0; i < count_lines; i++) 
                data = reader.readLine();

            if (data != null) { 
                data = data.strip().split(":", 2)[1];
            }
            return data; 
        } catch (Exception e) { 
            System.out.println("ERROR: There was an error calling GetTimestamp: " + e.getMessage() );
            return "";
        }    
    }


    /* PRIVATE FUNCTIONS */
    static float GetServerData(SOLAR_DATA property) { 
        // dont use this function to get the timestamp. This function only returns a float. 

        if (property == SOLAR_DATA.TIMESTAMP) { 
            return -1f; 
        }

        int count_lines = 13; 

        try {
            ProcessBuilder pb = new ProcessBuilder("cat", SunblockDataPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (int i = 0; i < count_lines; i++) {
                String data = reader.readLine();

                if (data != null) {
                    data = data.strip();
                    if (property == SOLAR_DATA.PVVOLTAGE && data.contains("PVVoltage")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.PVCURRENT && data.contains("PVCurrent")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.PVPOWER && data.contains("PVPower")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.BATTVOLTAGE && data.contains("BattVoltage")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.BATTCHARGECURRENT && data.contains("BattChargeCurrent")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.BATTCHARGEPOWER && data.contains("BattChargePower")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.LPOWER && data.contains("LoadPower")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.BATTREMAINING && data.contains("BattPercentage")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.BATTOVERALLCURRENT && data.contains("BattOverallCurrent")){
                        return GetValue(data);  
                    } else if (property == SOLAR_DATA.SYSTEMPOWERDRAW && data.contains("CPUPowerDraw")){
                        return GetValue(data);
                    }
                }
            } 
            return 7.7f;
        } catch (Exception e) { 
            System.out.println("Sunblock Error: Failed to complete DataQueryProcess::GetServerData. Reason:  " + e.getMessage());
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


}
