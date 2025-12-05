package Cinema;

import java.util.ArrayList;

public class Cinema {
   private String cinemaid;
   private String name;
   private ArrayList<Screen> screens;

   public Cinema(String cinemaid , String name){
       this.cinemaid=cinemaid;
       this.name=name;
       this.screens = new ArrayList<>();
   }

   public void addScreen(Screen screen){
       this.screens.add(screen);
   }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCinemaid() {
        return cinemaid;
    }

    public ArrayList<Screen> getScreens() {
        return screens;
    }
}
