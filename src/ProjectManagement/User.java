package ProjectManagement;

import java.util.ArrayList;

public class User implements Comparable<User>, UserReport_ {
    public ArrayList<Job> joblist=new ArrayList<>();
    public ArrayList<Job> alljoblist=new ArrayList<>();
    public ArrayList<Job> completedjoblist=new ArrayList<>();
    public String name;
    public int  consumed=0;
    public int  latest_cmp_time=0;

    public User(String name){
        this.name=name;
    }


    public int compareTo(User user) {
        if(this.consumed==user.consumed){
            if(this.latest_cmp_time<user.latest_cmp_time){
                return 1;
            }else {
                return -1;
            }

        }else{
            if(this.consumed>user.consumed){
                return 1;
            }else{
                return -1;
            }
        }

    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public String user() {
        return this.name;
    }

    @Override
    public int consumed() {
        return this.consumed;
    }

}
