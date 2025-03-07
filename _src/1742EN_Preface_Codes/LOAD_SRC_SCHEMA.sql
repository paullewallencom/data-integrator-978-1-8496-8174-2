-- Run this script as user DEMO_SRC --



Insert into SRC_DATABASE_LIST
   (SEQ, DATABASE_URL)
 Values
   (1, 'localhost:1521:ORCL11');
Insert into SRC_DATABASE_LIST
   (SEQ, DATABASE_URL)
 Values
   (2, 'localhost:1521:ORCL11');
Insert into SRC_DATABASE_LIST
   (SEQ, DATABASE_URL)
 Values
   (3, 'localhost:1521:ORCL11');
COMMIT;


----------------


Insert into SRC_DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (10, 'ACCOUNTING', 'NEW YORK');
Insert into SRC_DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (20, 'RESEARCH', 'DALLAS');
Insert into SRC_DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (30, 'SALES', 'CHICAGO');
Insert into SRC_DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (40, 'OPERATIONS', 'BOSTON');
COMMIT;

------------------


Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7369, 'SMITH', 'CLERK', 7902, TO_DATE('12/17/1980 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 800, 20);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7499, 'ALLEN', 'SALESMAN', 7698, TO_DATE('02/20/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1600, 300, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7521, 'WARD', 'SALESMAN', 7698, TO_DATE('02/22/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1250, 500, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7566, 'JONES', 'MANAGER', 7839, TO_DATE('04/02/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2975, 20);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7654, 'MARTIN', 'SALESMAN', 7698, TO_DATE('09/28/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1250, 1400, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7698, 'BLAKE', 'MANAGER', 7839, TO_DATE('05/01/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2850, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7782, 'CLARK', 'MANAGER', 7839, TO_DATE('06/09/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 2450, 10);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7788, 'SCOTT', 'ANALYST', 7566, TO_DATE('04/19/1987 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3000, 20);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, HIREDATE, SAL, DEPTNO)
 Values
   (7839, 'KING', 'PRESIDENT', TO_DATE('11/17/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 5000, 10);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7844, 'TURNER', 'SALESMAN', 7698, TO_DATE('09/08/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1500, 0, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7876, 'ADAMS', 'CLERK', 7788, TO_DATE('05/23/1987 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1100, 20);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7900, 'JAMES', 'CLERK', 7698, TO_DATE('12/03/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 950, 30);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7902, 'FORD', 'ANALYST', 7566, TO_DATE('12/03/1981 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 3000, 20);
Insert into SRC_EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7934, 'MILLER', 'CLERK', 7782, TO_DATE('01/23/1982 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 1300, 10);
COMMIT;


---------------------

INSERT INTO SRC_SALGRADE VALUES (1,700,1200);
INSERT INTO SRC_SALGRADE VALUES (2,1201,1400);
INSERT INTO SRC_SALGRADE VALUES (3,1401,2000);
INSERT INTO SRC_SALGRADE VALUES (4,2001,3000);
INSERT INTO SRC_SALGRADE VALUES (5,3001,9999);
COMMIT;


-------------

INSERT INTO SRC_BONUS ( ENAME, JOB, SAL, COMM ) VALUES ( 
'ALLEN', 'SALESMAN', 1600, 525); 
INSERT INTO SRC_BONUS ( ENAME, JOB, SAL, COMM ) VALUES ( 
'ALLEN', 'SALESMAN', 1600, 300); 
INSERT INTO SRC_BONUS ( ENAME, JOB, SAL, COMM ) VALUES ( 
'MARTIN', 'SALESMAN', 1250, 1300); 
COMMIT;


--------------

INSERT INTO SRC_WORK_HIST ( EMPNO, DATE_WORKED, HOURS ) VALUES ( 
7521,  TO_Date( '02/11/2013', 'MM/DD/YYYY'), 8); 
INSERT INTO SRC_WORK_HIST ( EMPNO, DATE_WORKED, HOURS ) VALUES ( 
7521,  TO_Date( '02/12/2013', 'MM/DD/YYYY'), 7.5); 


COMMIT;
