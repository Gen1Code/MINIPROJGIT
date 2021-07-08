package servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

@WebServlet(
        name = "HelloServlet",
        urlPatterns = {"/hello"}
    )
public class HelloServlet extends HttpServlet {
    JSONParser parser = new JSONParser();
    boolean winner = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Random rnd = new Random();
        JSONObject object = new JSONObject();
        try {
            FileReader fileR = new FileReader("src/main/webapp/data.json");
            object = (JSONObject) parser.parse(fileR);
            fileR.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int roll = rnd.nextInt(6)+1;
        int turn = ((Long) object.get("Turn")).intValue();
        int CCell = ((Long) ((JSONObject)((JSONArray) object.get("Players")).get(turn)).get("cell")).intValue();
        int NCell = CCell + roll;
        if( (boolean) ((JSONObject)object.get("Question")).get("AC")){
            NCell = NCell +2;
        }
        ((JSONObject)object.get("Question")).put("AD",false);
        ((JSONObject)object.get("Question")).put("AD",false);

        if(NCell ==19){
            NCell =4;
        }else if(NCell ==6){
            NCell = 24;
        }else if(NCell ==13){
            NCell = 7;
        }else if(NCell ==10){
            NCell = 12;
        }else if(NCell ==20){
            NCell = 38;
        }else if(NCell ==11){
            NCell = 33;
        }else if(NCell ==48){
            NCell = 14;
        }else if(NCell ==40){
            NCell = 59;
        }else if(NCell ==57){
            NCell = 36;
        }else if(NCell ==45){
            NCell = 54;
        }else if(NCell ==64){
            NCell = 78;
        }else if(NCell ==68){
            NCell = 49;
        }else if(NCell ==72){
            NCell = 91;
        }else if(NCell ==86){
            NCell = 96;
        }else if(NCell ==98){
            NCell = 84;
        }else if(NCell ==83){
            NCell = 61;
        }else if(NCell ==87){
            NCell = 66;
        }else if(NCell ==94){
            NCell = 88;
        }else if(NCell >=100){
            System.out.println("Player "+(turn +1) + " wins!!");
            winner = true;
        }

        File file = new File("src/main/webapp/Questions.txt");
        Scanner myReader = new Scanner(file);

        int QNOo= rnd.nextInt(5)+1;
        for (int i =0;i<QNOo;i++){
            myReader.nextLine();
        }
        String[] question = myReader.nextLine().split(",");
        myReader.close();

        ((JSONObject)((JSONArray) object.get("Players")).get(turn)).put("cell",NCell);

        ((JSONObject)object.get("Question")).put("Q",question[0]);
        ((JSONObject)object.get("Question")).put("A",question[1]);
        ((JSONObject)object.get("Question")).put("AD",false);

        ((JSONObject)object.get("Dice")).put("face",roll);
        int turnf = turn;
        turn = (turn +1)%((JSONArray)object.get("Players")).size();
        object.put("Turn",turn);
        JSONObject data;
        data = object;

        if(winner){
            FileReader fileR = new FileReader("src/main/webapp/data.json");
            try {
                data = (JSONObject) parser.parse(fileR);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fileR.close();

            ((JSONObject) data.get("Question")).put("Q", question[0]);
            ((JSONObject) data.get("Question")).put("A", question[1]);
            ((JSONObject) data.get("Question")).put("AD", false);
            ((JSONObject) data.get("Question")).put("AC", false);
            ((JSONObject)((JSONArray) data.get("Players")).get(0)).put("cell",1);
            ((JSONObject)((JSONArray) data.get("Players")).get(1)).put("cell",1);
            ((JSONObject)((JSONArray) data.get("Players")).get(2)).put("cell",1);
            ((JSONObject)((JSONArray) data.get("Players")).get(3)).put("cell",1);

            //Update leaderboard
            BufferedReader fileR2 = new BufferedReader(new FileReader("src/main/webapp/stats.txt"));
            ArrayList<String[]> stats = new ArrayList<>();
            ArrayList<String> PlayerNames = new ArrayList<>();
            ArrayList<String> PlayerNames2 = new ArrayList<>();
            for(int i=0;i<4;i++){
                PlayerNames.add((String) ((JSONObject)((JSONArray) object.get("Players")).get(i)).get("name"));
                PlayerNames2.add((String) ((JSONObject)((JSONArray) object.get("Players")).get(i)).get("name"));
            }
            boolean foundW = false;
            String winnerName = PlayerNames.get(turnf);
            String line = fileR2.readLine();
            while(line != null){
                String[] temp = line.split(",");
                if(temp[0].equals(PlayerNames2.get(turnf))){
                    foundW = true;
                    //Winning Index is 1
                    temp[1] = String.valueOf(Integer.parseInt(temp[1]) + 1);
                }
                if(temp[0].equals(PlayerNames2.get(0)) || temp[0].equals(PlayerNames2.get(1)) ||temp[0].equals(PlayerNames2.get(2))|| temp[0].equals(PlayerNames2.get(3))){
                    temp[2] = String.valueOf(Integer.parseInt(temp[2]) +1);
                    PlayerNames.remove(temp[0]);
                }
                stats.add(temp);
                line = fileR2.readLine();
            }
            fileR2.close();
            if(!foundW){
                stats.add(new String[]{PlayerNames2.get(turnf), "1", "1"});
                PlayerNames.remove(winnerName);
            }
            for(int i=0;i<PlayerNames.size();i++){
                stats.add(new String[]{PlayerNames.get(i),"0","1"});
            }
            data.put("Turn",0);

            //Write new stats to stats no
            BufferedWriter fileW2 = new BufferedWriter(new FileWriter("src/main/webapp/stats.txt"));
            for(int i =0;i<stats.size();i++){
                fileW2.write(stats.get(i)[0]+","+stats.get(i)[1]+","+stats.get(i)[2]);
                fileW2.newLine();
            }
            fileW2.flush();
            fileW2.close();
            //Why dont you work you piece of
            data.put("Winner",winnerName);
            winner = false;
        }

        FileWriter fileW = new FileWriter("src/main/webapp/data.json");
        fileW.write(data.toString());
        fileW.close();

        resp.getWriter().write(String.valueOf(data));

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }

        String Answer = buffer.toString().replaceAll("\"","").trim().toLowerCase();

        System.out.println(Answer);
        JSONObject data = new JSONObject();
        FileReader fileR = new FileReader("src/main/webapp/data.json");
        try {
            data = (JSONObject) parser.parse(fileR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fileR.close();

        ((JSONObject)data.get("Question")).put("AD",true);

        if(((String)((JSONObject)data.get("Question")).get("A")).equals(Answer)){
            ((JSONObject)data.get("Question")).put("AC",true);
        }else{
            ((JSONObject)data.get("Question")).put("AC",false);
        }

        FileWriter fileW = new FileWriter("src/main/webapp/data.json");
        fileW.write(data.toString());
        fileW.close();

        resp.getWriter().write(String.valueOf(data));

    }

}
