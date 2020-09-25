package dao;

import org.junit.Test;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DBHelperStressTest {
    AtomicInteger NoteNumber = new AtomicInteger(100);
    volatile static boolean pass = true;
    @Test
    public void testAddDeleteSchool() throws Exception {
        System.out.println("addSchool then deleteSchool stress test");

        class WorkerThread implements Runnable {

            @Override
            public void run() {
                try {
                    int num = NoteNumber.incrementAndGet();
                    //String header = "header" + num;
                    //String name = "name" + num;
                    //String description = "description" + num;
                    String email = "email" + num + "@mail.ru";
                    //DBHelper.createUser(name, header, name, email);
                    //Integer id = DBHelper.addNote(header,name, description, email).getId();
                    //List<Batch> batchBySchool = DBHelper.getBatchBySchool(id);
                    //DBHelper.deleteNote(id);
                    DBHelper.deleteUser(email);
                    if (num % 10000 ==0)
                        //to avoid suppression of this message by logback-test.xml settings
                        System.out.println("Performed test #"+num);

                } catch (SQLException ex) {
                    Logger.getLogger(DBHelperStressTest.class.getName()).log(Level.SEVERE, null, ex);
                    pass = false;
                    fail("SQL error executing stress test");
                }
            }
        }

        DBHelper.init();

        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000000; i++) {
            Runnable worker = new WorkerThread();
            executor.execute(worker);
        }

        executor.shutdown();

        while (!executor.isTerminated());

        if (!pass) fail("Exception in one of worker threads");
        Logger.getLogger(DBHelperStressTest.class.getName()).log(Level.INFO, null, "Finished executing stress test");

    }
}