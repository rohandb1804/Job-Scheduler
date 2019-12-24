package ProjectManagement;
import PriorityQueue.*;
public class Job implements Comparable<Job>, JobReport_ {

    int runningtime;
    int endtime=0;
    int arrivaltime=0;
    Project project;
    Integer priority;
    User user;
    String name;
    boolean status;
    Integer count;

    Job(int run, Project pro, User use, String name ,int count){
        runningtime=run;
        project=pro;
        user=use;
        this.name=name;
        this.status=false;
        this.count=count;
    }
    @Override
    public int compareTo(Job job) {
      /* if(project.priority.compareTo(job.project.priority)==0){
            return (-1)*(count.compareTo(job.count));
        }

        return project.priority.compareTo(job.project.priority);*/

      if(priority.compareTo(job.priority)==0){
          return (-1)*(count.compareTo(job.count));
      }
      return this.priority.compareTo(job.priority);
    }

    @Override
    public String user() {
        return user.name;
    }

    @Override
    public String project_name() {
        return project.name;
    }

    @Override
    public int budget() {
        return this.runningtime;
    }

    @Override
    public int arrival_time() {
        return arrivaltime;
    }

    @Override
    public int completion_time() {
        return endtime;
    }
}