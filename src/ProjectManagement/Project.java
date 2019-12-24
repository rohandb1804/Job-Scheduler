package ProjectManagement;


import java.util.ArrayList;

public class Project implements Comparable<Project> {
    public String name;
    public Integer budget;
    public Integer priority;
    public Integer count;
    public ArrayList<Job> alljobArrayList=new ArrayList<>();
    public ArrayList<Job> completedjobArrayList=new ArrayList<>();

    Project(int priority,int count){
        this.priority=priority;
        this.count=count;
    }



    public int compareTo(Project project) {
        if (project.priority==this.priority){
             return (-1)*(this.count.compareTo(project.count));
        }
        return priority.compareTo(project.priority);

    }

    @Override
    public String toString() {
        return name;
    }

}
