package ProjectManagement;
import PriorityQueue.*;
import RedBlack.*;
import Trie.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Scheduler_Driver extends Thread implements SchedulerInterface {

    public static void main(String[] args) throws IOException {
        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        Timer timer =new Timer();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }



    public void execute(File commandFile) throws IOException {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }

                switch (cmd[0]) {
                    case "PROJECT":
                        handle_project(cmd);
                        break;
                    case "JOB":
                        handle_job(cmd);
                        break;
                    case "USER":
                        handle_user(cmd[1]);
                        break;
                    case "QUERY":
                        handle_query(cmd[1]);
                        break;
                    case "":
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":

                    case "NEW_USER":

                    case "NEW_PROJECTUSER":

                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                        System.out.println("Top Query");
                        long qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        long qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                        System.out.println("Flush Query");
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;

                    default:
                        System.err.println("Unknown command: " + cmd[0]);
                }
            }

            run_to_completion();
            print_stats();
        }catch (FileNotFoundException e){
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        }catch(NullPointerException ne) {
            ne.printStackTrace();

        }

    }

    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
                System.out.println("Project Query");
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_USER":
                System.out.println("User Query");
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));

                break;
            case "NEW_PROJECTUSER":
                System.out.println("Project User Query");
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
                System.out.println("Priority Query");
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }

        return res;
    }


    public ArrayList<UserReport_> timed_top_consumer(int top) {
       // print_stats();
        int size=0;
       ArrayList<UserReport_> userReportList=new ArrayList<>();
       MaxHeap<User> userMaxHeap=new MaxHeap<>();

        for(int i=0;i<userlist.size();i++){
          userMaxHeap.insert(userlist.get(i));
      }
      if(top<=userMaxHeap.size()){
          size=top;
      }else{
          size=userMaxHeap.size();
      }
      while (size!=0){
          User user=userMaxHeap.extractMax();
          System.out.println(user.name+ " "+ user.consumed+ " "+user.latest_cmp_time);
          userReportList.add(user);
          size--;
      }


        return userReportList;
    }



    public static final int maxpriority=9999;
    @Override
    public void timed_flush(int waittime) {
        System.out.println(global);
        for(int i=0;i<jobMaxHeap.size();i++){
            System.out.println(jobMaxHeap.array.get(i).data.name +" "+ jobMaxHeap.array.get(i).data.arrivaltime+ " "+ jobMaxHeap.array.get(i).data.project.budget);
        }

     MaxHeap<Job> heapsortedjob=new MaxHeap<>();
      ArrayList<Job> flushedjobs=new ArrayList<>();
      //jobmaxheap is the original heap of jobs
      for(int i=0;i<jobMaxHeap.size();i++){
          Job job =jobMaxHeap.array.get(i).data;
          if((global-job.arrivaltime)>=waittime && job.project.budget>=job.runningtime ){
              heapsortedjob.insert(job);
          }

      }

        while(heapsortedjob.size()!=0){
            Job job=heapsortedjob.extractMax();
            //System.out.println(job.name+ "-----"+ job.project.budget);
            for(int i=0;i<jobMaxHeap.size();i++){
                Job job2 =jobMaxHeap.array.get(i).data;
                if(job2.count.equals(job.count) && job.project.budget>=job.runningtime){
                    jobMaxHeap.array.get(i).data.priority=maxpriority;
                    jobMaxHeap.move_up(i);
                    jobMaxHeap.extractMax();
                    System.out.println(job.name);
                   // flushedjobs.add(flushjob);
                    job.status=true;
                    global+=job.runningtime;
                    job.endtime=global;
                    job.priority=job.project.priority;
                    // User user=((User)userTrie.search(job.username).getValue());
                    job.user.consumed+=job.runningtime;
                    if(job.user.latest_cmp_time<job.endtime){
                        job.user.latest_cmp_time=job.endtime;
                    }
                    job.project.completedjobArrayList.add(job);
                    job.user.joblist.add(job);
                    job.user.completedjoblist.add(job);
                    jobs.add(job);
                  //  System.out.println(job.name);
                    job.project.budget-=job.runningtime;
                }
            }

        }




    }


    private ArrayList<JobReport_> handle_new_priority(String s) {

        int threshold=Integer.parseInt(s);
        ArrayList<JobReport_> jobreportlist=new ArrayList<>();
    for(int i=0;i<allJobs.size();i++){
            Job job=allJobs.get(i);
            if(allJobs.get(i).status==false){
                if(job.priority>=threshold){
                 //  System.out.println(job.name);
                    jobreportlist.add(job);
                }
            }
        }


     /*   Queue<RedBlackNode> queue=new LinkedList<>();
        if(projectJobRBTree.root!=null) {
            queue.add(projectJobRBTree.root);
            // System.out.println(projectJobRBTree.size());
            while (!queue.isEmpty()) {
                RedBlackNode redBlackNode = queue.poll();
                if (redBlackNode != null) {
                    //  System.out.println(redBlackNode.key.toString()+ "jobs:");
                    if (redBlackNode.getValues() != null) {
                        for (int i = 0; i < redBlackNode.getValues().size(); i++) {
                            Job job = (Job) redBlackNode.getValues().get(i);
                            if (job.priority >= Integer.parseInt(s)) {
                              //  System.out.println(((Job) redBlackNode.getValues().get(i)).name);
                                jobreportlist.add((Job) redBlackNode.getValues().get(i));
                            }
                        }

                    }


                }

                if (redBlackNode.left != null) {
                    queue.add(redBlackNode.left);
                }

                if (redBlackNode.right != null) {
                    queue.add(redBlackNode.right);
                }

            }
        }

        for(int i=0;i<jobMaxHeap.size();i++){
            Job job =jobMaxHeap.array.get(i).data;
            if(job.priority>=Integer.parseInt(s)){
               // System.out.println(job.name);
                jobreportlist.add(job);
            }
        }*/
     /*   for(int i=0;i<jobreportlist.size();i++){
          //  System.out.println(jobreportlist.get(i).user());
        }*/

        return jobreportlist;
    }

    public ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
        ArrayList<JobReport_> jobreportlist=new ArrayList<>();
        TrieNode node=projectTrie.search(cmd[1]);
        int farrival=Integer.parseInt(cmd[3]);
        int larrival=Integer.parseInt(cmd[4]);

        if(node!=null){
            Project project=((Project) node.getValue());
            if(project.completedjobArrayList!=null) {
                for(int i=0;i<project.completedjobArrayList.size();i++) {
                    Job job = project.completedjobArrayList.get(i);
                    if (job.user.name.equals(cmd[2])) {
                        if (job.arrivaltime >= farrival && job.arrivaltime <= larrival) {
                            // System.out.println(job.name + job.arrivaltime);
                            //System.out.println(job.name);
                            jobreportlist.add(job);
                        }
                    }
                }
            }
        }


        if(node!=null){
            Project project=((Project) node.getValue());
            if(project.alljobArrayList!=null) {
                for(int i=0;i<project.alljobArrayList.size();i++) {
                    Job job = project.alljobArrayList.get(i);
                    if (job.user.name.equals(cmd[2]) && job.status==false && job.arrivaltime >= farrival && job.arrivaltime <=larrival) {

                        jobreportlist.add(job);

                    }
                }
            }
        }



        return jobreportlist;


    }

    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
       ArrayList<JobReport_> jobreportlist=new ArrayList<>();
        TrieNode node=userTrie.search(cmd[1]);
        int farrival=Integer.parseInt(cmd[2]);
        int larrival=Integer.parseInt(cmd[3]);

        if(node!=null){
            User user=((User) node.getValue());
           if(user.alljoblist!=null) {
                for(int i=0;i<user.alljoblist.size();i++){
                    Job job=user.alljoblist.get(i);
                    if(job.arrivaltime>=farrival && job.arrivaltime<=larrival) {
                     //   System.out.println(job.name + " " + job.arrivaltime + " "+ job.count);
                        //System.out.println(job.name);
                        jobreportlist.add(job);
                    }
                }
            }
        }

        return jobreportlist;
    }

    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
        ArrayList<JobReport_> jobreportlist=new ArrayList<>();
        TrieNode node=projectTrie.search(cmd[1]);
        int farrival=Integer.parseInt(cmd[2]);
        int larrival=Integer.parseInt(cmd[3]);

       if(node!=null){
           Project project=((Project) node.getValue());
          if(project.alljobArrayList!=null) {
              for(int i=0;i<project.alljobArrayList.size();i++){
                  Job job=project.alljobArrayList.get(i);
                  if(job.arrivaltime>=farrival && job.arrivaltime<=larrival) {
                   //  System.out.println(job.name + job.arrivaltime);
                      //System.out.println(job.name);
                      jobreportlist.add(job);
                  }
              }
          }
       }
        return jobreportlist;

    }



    public MaxHeap<Job> jobMaxHeap=new MaxHeap<Job>();
    ArrayList<User> userlist=new ArrayList<>();
    Trie<Job> trie=new Trie<Job>();
    Trie<Project> projectTrie=new Trie<Project>();
    Trie<User> userTrie=new Trie<User>();
    RBTree<String, Job> projectJobRBTree=new RBTree<>();
    ArrayList<Job> jobs=new ArrayList<>();
    ArrayList<Job> allJobs =new ArrayList<>();
    ArrayList<Project> allProjets=new ArrayList<>();
    ArrayList<Job> uncompletejobs=new ArrayList<>();
   // RBTree<String,Job> alljobRBTree =new RBTree<>();
   // RBTree<String,Job> userJobRBTree=new RBTree<>();
   // RBTree<String,Job> userJobRb2=new RBTree<>();

    int global=0;
    @Override
    public void run() {
        // till there are JOBS

    }


    @Override
    public void run_to_completion() {
        while (jobMaxHeap.size()!=0){
            int count=0;
            System.out.println("Running code");
            System.out.println("Remaining jobs: " +jobMaxHeap.size());
            while(jobMaxHeap.size()!=0) {
                Job job = jobMaxHeap.extractMax();
                System.out.println("Executing: " + job.name + " from: " + job.project.name);

                if (job.runningtime <= job.project.budget) {
                    //  Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_time=10, end_time=10, name=’DeepLearning’}
                    //  System.out.println(job.project.budget);
                    //  System.out.println(job.runningtime);
                    //  job.project.budget -= job.runningtime;
                    job.status=true;
                 /*   for(int i=0;i<jobuncomplete.size();i++){
                        if(jobuncomplete.get(i).status){
                            jobuncomplete.remove(i);
                        }
                    }*/

                    jobs.add(job);
                    global+=job.runningtime;
                    job.endtime=global;
                    User user=((User)userTrie.search(job.user.name).getValue());
                    user.consumed+=job.runningtime;
                    if(user.latest_cmp_time<job.endtime){
                        user.latest_cmp_time=job.endtime;
                    }
                    ((User)userTrie.search(job.user.name).getValue()).joblist.add(job);
                    job.project.completedjobArrayList.add(job);
                    // System.out.println(RBjob.search(job.name).getValues().get(0).status);
                    ((Project) projectTrie.search(job.project.name).getValue()).budget -= job.runningtime;
                    System.out.println("Project: " + job.project.name + " " + "budget remaining: " + job.project.budget);
                    System.out.println("System execution completed");
                    //  System.out.println("");
                    break;
                }else {
                    job.status=false;
                    uncompletejobs.add(job);
                    System.out.println(	"Un-sufficient budget.");

                }
            }

        }
        //schedule();
        return ;
    }
    int projcount=0;
    @Override
    public void handle_project(String[] cmd) {

        Project project =new Project(Integer.parseInt(cmd[2]),0);
        project.budget=Integer.parseInt(cmd[3]);
        project.name=cmd[1];
        project.count=projcount;
        projcount++;
        projectTrie.insert(cmd[1],project);
        allProjets.add(project);
        System.out.println("Creating project");

    }

    int count=0;
    @Override
    public void handle_job(String[] cmd) {
        System.out.println("Creating job" );
        Job job= new Job(Integer.parseInt(cmd[4]),new Project(9999,0),new User(null),cmd[1],0);
        if(userTrie.search(cmd[3])==null || projectTrie.search(cmd[2])==null){
            if(userTrie.search(cmd[3])==null) {
                System.out.println("No such user exists: " + cmd[3]);
            }
            else if(projectTrie.search(cmd[2])==null){
                System.out.println("No such project exists. " + cmd[2]);
            }
            return;
        }
           // int index =userlist.indexOf(new User(cmd[3]));
            job.user=((User)userTrie.search(cmd[3]).getValue());
           // job.user=new User(cmd[3]);
           // job.user.name=cmd[3];
            job.user.alljoblist.add(job);
            // userlist.get(index).joblist.add(job);
            job.project=(Project)projectTrie.search(cmd[2]).getValue();
            job.project.alljobArrayList.add(job);
            job.priority=job.project.priority;

           // ((Project) projectTrie.search(cmd[2]).getValue()).jobArrayList.add(job);


                job.count=count;
                count++;
                job.arrivaltime=global;
                //  System.out.println(job);
                trie.insert(job.name,job);
                allJobs.add(job);
                jobMaxHeap.insert(job);
                // System.out.println(jobMaxHeap.size()+ " "+ job.project.priority);


    }

    @Override
    public void handle_user(String name) {
        User user=new User(name);
        userlist.add(user);
        userTrie.insert(name,user);
        System.out.println("Creating user" );


    }

    @Override
    public void handle_query(String key) {
        System.out.println("Querying");
        System.out.print(key+ ":");
        TrieNode trieNode=trie.search(key);
        if(trieNode!=null){
            boolean status1=((Job)trieNode.getValue()).status;
            if (status1 == true) {
                System.out.println(" COMPLETED");
            } else {
                System.out.println(" NOT FINISHED");
            }
        }else{
            System.out.println(" NO SUCH JOB");

        }
        /*RedBlackNode search = RBjob.searcher(key);
        if(search!=null) {
            if (search.getValues() != null) {
                boolean status = RBjob.search(key).getValues().get(0).status;
                if (status == true) {
                    System.out.println(" COMPLETED");
                } else {
                    System.out.println(" NOT FINISHED");
                }
            }
        }
        else{
            System.out.println(" NO SUCH JOB");
        }*/
    }

    @Override
    public void handle_empty_line() {

        schedule();


    }


    @Override
    public void handle_add(String[] cmd) {
        TrieNode node= projectTrie.search(cmd[1]);
        if(node==null){
            System.out.println("No such project exists "+ cmd[1]);
            return;
        }
        System.out.println("ADDING Budget");

        //  System.out.println(((Project)node.getValue()).budget);
        ((Project)node.getValue()).budget+=Integer.parseInt(cmd[2]);


      /*  while(jobMaxHeap.size()!=0){
            Job job=jobMaxHeap.extractMax();
            tmpHeap.insert(job);
        }*/
        RedBlackNode rbnode=projectJobRBTree.search(node.getValue().toString());
        if (rbnode!=null){
            if(rbnode.getValues()!=null) {
                for (int i = 0; i < rbnode.getValues().size(); i++) {

                    jobMaxHeap.insert((Job)rbnode.getValues().get(i));

                }
                projectJobRBTree.search(node.getValue().toString()).getValues().clear();
            }

    }
       /* ArrayList<Job> arrayList=new ArrayList<>();
        for(int i=0;i<uncompletejobs.size();i++){
            if(uncompletejobs.get(i).project.name.equals(cmd[1])){
                System.out.println(uncompletejobs.get(i).name + " "+ uncompletejobs.get(i).project.name);
                jobMaxHeap.insert(uncompletejobs.get(i));
            }else{
                   arrayList.add(uncompletejobs.get(i));
                   System.out.println("not " +uncompletejobs.get(i).name + " " +uncompletejobs.get(i).project.name);


            }

        }
        uncompletejobs=arrayList;*/


    }

    @Override
    public void print_stats() {
        System.out.println("--------------STATS---------------");
        System.out.println("Total jobs done: "+ jobs.size());
        //Job{user=’Rob’, project=’IITD.CS.ML.ICML’, jobstatus=COMPLETED, execution_time=10, end_time=10, name=’DeepLearning’}
        for(int i=0;i<jobs.size();i++){
            Job job=jobs.get(i);
            //System.out.println("Job{user='"+ job.user.name + "', project='" + job.project.name+ "', jobstatus=COMPLETED, execution_time="+ job.runningtime+ ", end_time="+ job.endtime+ ", name='"+ job.name+ "'}"+ job.arrivaltime);
            System.out.println("Job{user='"+ job.user.name+ "', project='" + job.project.name+ "', jobstatus=COMPLETED, execution_time="+ job.runningtime+ ", end_time="+ job.endtime+ ", name='"+ job.name+ "'}");
        }
        System.out.println("------------------------");
        System.out.println("Unfinished jobs: ");
        MaxHeap<Job> uncompJob=new MaxHeap<>();

        int counter=0;

        MaxHeap<Project> projectMaxHeap=new MaxHeap<>();
        for(int i=0;i<allProjets.size();i++){
            projectMaxHeap.insert(allProjets.get(i));
        }
        while(projectMaxHeap.size()!=0){
            Project project=projectMaxHeap.extractMax();
            for(int i=0; i<project.alljobArrayList.size();i++){
                Job job=project.alljobArrayList.get(i);
                if(job.status==false) {
                    counter++;
                    System.out.println("Job{user='" + job.user.name + "', project='" + job.project.name + "', jobstatus=REQUESTED, execution_time=" + job.runningtime + ", end_time=null" +  ", name='" + job.name + "'}");
                }
            }
        }


        System.out.println("Total unfinished jobs: " +counter);
       // System.out.println("Total jobs done: "+ jobs.size());
        System.out.println("--------------STATS DONE---------------");
    }


    public void schedule() {
        System.out.println("Running code");
        if(jobMaxHeap.size()!=0) {
            System.out.println("Remaining jobs: " + jobMaxHeap.size());
        }
        while(jobMaxHeap.size()!=0) {
            // System.out.println("Remaining jobs: " +jobMaxHeap.size());
            Job job = jobMaxHeap.extractMax();
            System.out.println("Executing: " + job.name + " from: " + job.project.name);
            if (job.runningtime <= job.project.budget) {

                job.status=true;
                global+=job.runningtime;
                job.endtime=global;
               // User user=((User)userTrie.search(job.username).getValue());
                job.user.consumed+=job.runningtime;
                if(job.user.latest_cmp_time<job.endtime){
                    job.user.latest_cmp_time=job.endtime;
                }
                job.project.completedjobArrayList.add(job);
                job.user.joblist.add(job);
                job.user.completedjoblist.add(job);
                jobs.add(job);
                //System.out.println(RBjob.search(job.name).getValues().get(0).status);
                job.project.budget -= job.runningtime;
                System.out.println("Project: " + job.project.name + " " + "budget remaining: " + job.project.budget);
                break;
            }else {
                job.status=false;
                // System.out.println(RBjob.search(job.name).getValues().get(0).status);
                projectJobRBTree.insert(job.project.name,job);
               // uncompletejobs.add(job);
                System.out.println("Un-sufficient budget.");
            }
        }
        System.out.println("Execution cycle completed");
       // print_stats();
    }
    public  void timed_handle_user(String name){
        User user=new User(name);
        userlist.add(user);
        userTrie.insert(name,user);

    }
    public void timed_handle_job(String[] cmd){
        Job job= new Job(Integer.parseInt(cmd[4]),new Project(9999,0),new User(null),cmd[1],0);
        if(userTrie.search(cmd[3])==null || projectTrie.search(cmd[2])==null){
           /* if(userTrie.search(cmd[3])==null) {
                System.out.println("No such user exists: " + cmd[3]);
            }
            else if(projectTrie.search(cmd[2])==null){
                System.out.println("No such project exists. " + cmd[2]);
            }*/
            return;
        }

        job.user=((User)userTrie.search(cmd[3]).getValue());
      //  job.username=cmd[3];
        job.user.alljoblist.add(job);
        job.project=(Project)projectTrie.search(cmd[2]).getValue();
        job.project.alljobArrayList.add(job);
        job.priority=job.project.priority;
        job.count=count;
        count++;
        job.arrivaltime=global;
        trie.insert(job.name,job);
        allJobs.add(job);
        //alljobRBTree.insert(job.project.name,job);
       // userJobRBTree.insert(job.user.name,job);

        jobMaxHeap.insert(job);

    }
    public void timed_handle_project(String[] cmd){

        Project project =new Project(Integer.parseInt(cmd[2]),0);
        project.budget=Integer.parseInt(cmd[3]);
        project.name=cmd[1];
        project.count=projcount;
        projcount++;
        projectTrie.insert(cmd[1],project);
        allProjets.add(project);
    }
    public void timed_run_to_completion(){

        while(jobMaxHeap.size()!=0) {
            // System.out.println("Remaining jobs: " +jobMaxHeap.size());
            Job job = jobMaxHeap.extractMax();
            if (job.runningtime <= job.project.budget) {
                job.status=true;
                global+=job.runningtime;
                job.endtime=global;
             //   User user=((User)userTrie.search(job.username).getValue());
                job.user.consumed+=job.runningtime;
                if(job.user.latest_cmp_time<job.endtime){
                    job.user.latest_cmp_time=job.endtime;
                }
                job.project.completedjobArrayList.add(job);
                job.project.budget -= job.runningtime;
                job.user.joblist.add(job);
                job.user.completedjoblist.add(job);
                jobs.add(job);



                break;
            }else {
                job.status=false;
                projectJobRBTree.insert(job.project.name,job);
               // uncompletejobs.add(job);




            }
        }


    }


}
