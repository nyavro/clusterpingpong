Sample AKKA cluster

To run on local machine:
1. Open terminal for Frontend
2. Run activator
3. runMain pingpong.Frontend
4. Open terminal for Backend 1
5  Run activator
6  runMain pingpong.Backend 2551 777
7  Open terminal for Backend 2
8  Run activator
9  runMain pingpong.Backend 2552 1111

See the result like:
[info] Don't disturb me with Job-25! I'm doing only very important job on node 777!
[info] Very important job is done on node 1111
[info] Don't disturb me with Job-26! I'm doing only very important job on node 777!
[info] Don't disturb me with Job-27! I'm doing only very important job on node 1111!
[info] Very important job is done on node 777
[info] Very important job is done on node 1111
[info] Don't disturb me with Job-28! I'm doing only very important job on node 777!
