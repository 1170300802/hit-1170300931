package simulator;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;
import strategy.ChoseBridge;
import strategy.Closestrealspeed;
import strategy.Strategyone;
import strategy.TheClosestSpeed;

/**
 * ���ӹ���ģ����.
 * @author ����
 *
 */
public class Simulator {
  public static  Map<Ladder, List<Map<Rung, Monkey>>> ladders = Collections.synchronizedMap(new HashMap<>());// ����
  public static  List<Monkey> monkeys = Collections.synchronizedList(new ArrayList<>());// ����
  private  List<String[]> datas = new ArrayList<String[]>();// ��������(����ʱ�䣬ID�� direction��v)
  private  List<Thread> subthreads = new ArrayList<Thread>();// �߳�
  private int count = 0;// ��¼���ӱ��
  public static CountDownLatch threadSignal = null;//��ʼ��countDown
  public static List<Monkey> monkeyssequence = Collections.synchronizedList(new ArrayList<>());// �����ϰ�����
  public static FileWriter fw;
  private ChoseBridge strategy;// ����
  private static Scanner in = new Scanner(System.in);

  
  // RI:
  //    ladders��һ�������򳤶Ȳ��ܷ����ı�
  //    monkeys����ʼ�ͽ�����Ϊ��
  //    datas��һ�����ɲ��ܸı�
  //    count����������Ȼ���������ں��Ӹ���
  //    monkeyssequence����󳤶�Ϊ��������
  //
  // AF:
  //    ladders��ʾ��������.
  //    monkeys���������������б�
  //    datas�������������ݼ�
  //    subthreads�洢���������߳��б�
  //    monkeyssequence�����ӹ��ӽ����ʱ��˳�����򱣴�
  //
  // rep from exposure:
  //    datas, subthreads, countΪprivate��.
  
  /**
   * ��������.
   * @param n ���Ӹ�����Ϊһ����Ȼ��.
   * @param h ÿ�������ϵ�̤��������ʼ��Ϊ20.
   */
  private void generateLadders(int n, int h) {
    Ladder l = null;// ����
    
    for(int i = 1;i <= n;i++) {
      List<Map<Rung, Monkey>> temp = Collections.synchronizedList(new ArrayList<Map<Rung,Monkey>>());// ̤��:����
      for (int j = 1; j <= h; j++) {
        Map<Rung, Monkey> map = Collections.synchronizedMap(new HashMap<>());// ̤��:����
        map.put(new Rung(j), null);
        temp.add(map);
      }
      l = new Ladder(i, "");
      ladders.put(l, temp);
    }
    
    // ��ĳ��һ������һֻ����
//    ladders.get(new Ladder(1)).get(19).put(new Rung(20), new Monkey(100, 10, ladders));
  }
  
  /**
   * ����datas���ɺ��ӱ�����monkeys��.
   * @throws InterruptedException �����Ƿ񱻴��.
   */
  private void generateMonkeys() throws InterruptedException {
    try {
      fw = new FileWriter(new File("src/output/log.txt"));
      fw.write("");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("��־�ļ���ʧ�ܣ�");
    }
    threadSignal = new CountDownLatch(datas.size());
    // ���ɺ���
    Monkey monkey = null;
    int allmonkeysnum = datas.size();
    int circletime = 0;
    int permonkeysnum = allmonkeysnum;
    // ���ҵ�����ʱ�������������
    for(int i = 0;i < allmonkeysnum-1;i++) {
      if(!datas.get(i)[0].equals(datas.get(i+1)[0])) {
        permonkeysnum = i+1;
        circletime = Integer.valueOf(datas.get(i+1)[0]);
        break;
      }
    }
    
    // ��ʼ����
    int batch = 1;
    strategy = choseStrategy();
    System.out.println("���ι��Ӻ�������Ϊ: " + datas.size() + "ֻ");
    Simulator.writelog("���ι��Ӻ�������Ϊ: " + datas.size() + "ֻ");
    System.out.println("��������Ϊ��" + ladders.size());
    Simulator.writelog("��������Ϊ��" + ladders.size());
    System.out.println("ÿ" + circletime + "��" + "����" + permonkeysnum + "ֻ����");
    Simulator.writelog("ÿ" + circletime + "��" + "����" + permonkeysnum + "ֻ����");
    Random random = new Random();// ���ѡ�����
    
    // ��¼���к���״̬
    for(String[] s:datas) {
      Simulator.writelog("IDΪ" + s[1] + "�ĺ��ӷ���Ϊ" + s[2] + ",�ٶ�Ϊ" + s[3]);
    }
    Thread.sleep(1000);
    System.out.println("����������������");
    while (count < allmonkeysnum) {
      System.out.println("����������" + batch + "���С�������");
      batch++;
      // ����������N-k��
      if(count + permonkeysnum > allmonkeysnum) {
        for(int i = 0;i < allmonkeysnum - count;i++) {
          if(strategy == null) {
            int strat = random.nextInt(3);
            if(strat == 0) {
              monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new Strategyone());
            }
            if(strat == 1) {
              monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new TheClosestSpeed());
            }
            if(strat == 2) {
              monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new Closestrealspeed());
            }
          }else {
            monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), strategy);
          }
          monkeys.add(monkey);
          count++;
        return;
        }
      }
      
      // ����������k��
      for(int i = 0;i < permonkeysnum;i++) {
        if(strategy == null) {
          int strat = random.nextInt(3);
          if(strat == 0) {
            monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new Strategyone());
          }
          if(strat == 1) {
            monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new TheClosestSpeed());
          }
          if(strat == 2) {
            monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), new Closestrealspeed());
          }
        }else {
          monkey = new Monkey(Integer.valueOf(datas.get(count)[1]), Integer.valueOf(datas.get(count)[3]), ladders, datas.get(count)[2], threadSignal, Integer.valueOf(datas.get(count)[0]), strategy);
        }        
        monkeys.add(monkey);
        count++;
      }
      Thread.sleep(circletime*1000);
    }
    System.out.println("������ɡ�");
  }
  
  /**
   * ��ʼ����ģ�����.
   */
  private void startsimulator() {
    for(Monkey monkey:monkeys) {
      Thread thread = new Thread(monkey);
      subthreads.add(thread);
      thread.start();
    }
  }
 
  /**
   * �����ǰ�������.
   */
  private static void print() {
    for(Ladder ladder:ladders.keySet()) {
      List<Map<Rung, Monkey>> rungs = ladders.get(ladder);
      Map<Rung, Monkey> rung = null;
      for(int i = 0;i < rungs.size();i++) {
        rung = rungs.get(i);
        for(Rung r:rung.keySet()) {
          if(rung.get(r) == null) {
            System.out.print("---  ");
          } else {
            System.out.print(String.format("%03d", rung.get(r).getID()) + "  ");
          }
        }
      }
      System.out.println();
    }
    System.out.println();
  }
  
  /**
   * д��־.
   * @param context ����.
   */
  public static void writelog(String context) {
     try {
      fw.write(context + "\n");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("�ļ�д��ʧ�ܣ�");
    }
  }
  
  /**
   * ͨ���ļ�����ģ����.
   * @throws InterruptedException
   */
  private void readData() throws InterruptedException {
    int choice;
    
    System.out.println("**********");
    System.out.println("1:Competition_1.txt");
    System.out.println("2:Competition_2.txt");
    System.out.println("3:Competition_3.txt");
    System.out.println("4:�����ļ���");
    System.out.println("5:Competition_4.txt");
    choice = in.nextInt();
    
    switch (choice) {
      case 1:
        fileData("Competition_1.txt");
        break;
      case 2:
        fileData("Competition_2.txt");
        break;
      case 3:
        fileData("Competition_3.txt");
        break;
      case 4:
        System.out.println("�������ļ���:");
        in.nextLine();
        String filename = in.nextLine();
        fileData(filename);
        break;
      case 5:
        fileData("Competition_4.txt");
        break;
      default:
        System.out.println("��������");
        break;
    }
    
  }
  
  /**
   * ���ļ���ȡ��Ϣ.
   * @param filename �ļ���.
   * @throws InterruptedException
   */
  private void fileData(String filename) throws InterruptedException {
    int rungsnum = 0;
    int laddersnum = 0;
    
    File file = new File("src/input/" + filename);
    try {
      BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      String line;
      while ((line = bf.readLine()) != null) {
        // ������
        if(line.startsWith("n")) {
          laddersnum = Integer.valueOf(line.split("=")[1]);
        } else if (line.startsWith("h")) {// ̤����
          rungsnum = Integer.valueOf(line.split("=")[1]);
        } else {// ����
          int length = line.length();
          String[] data = line.substring(8, length-1).split(",");// ��ȡ����
          datas.add(data);
        }
      }
      bf.close();
      // ��������
      generateLadders(laddersnum, rungsnum);
      
      // ��ʼ���ɺ���
      generateMonkeys();
    } catch (FileNotFoundException e) {
      System.out.println("�ļ���ʧ�ܡ�");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("�ļ��ر�ʧ�ܡ�");
    }
  }
  
  /**
   * ����ѡ��.
   * @return ��ѡ�Ĳ���.
   */
  private ChoseBridge choseStrategy() {
    while (true) {
      System.out.println("*************");
      System.out.println("��ѡ����ԣ�");
      System.out.println("1:strategy1");
      System.out.println("2:The closest speed I");
      System.out.println("3:The closest speed II");
      System.out.println("4:���ѡ�����");
      int choice = in.nextInt();
      
      switch (choice) {
        case 1:
          return new Strategyone();
        case 2:
          return new TheClosestSpeed();
        case 3:
          return new Closestrealspeed();
        case 4:
          return null;
        default:
          System.out.println("����������������룡");
          break;
      }
    }
  }
  
  /**
   * ����������.
   * @throws InterruptedException
   */
  private void inputData() throws InterruptedException {
    int rungsnum = 20;
    System.out.println("���������Ӹ���(1-10)��");
    int laddersnum = in.nextInt();
    System.out.println("������̤�����(Ĭ��20)��");
    rungsnum = in.nextInt();
    System.out.println("�������������ʱ��(1-5)��");
    int circletime = in.nextInt();
    System.out.println("�������������(2-1000)��");
    int allmonkeysnum = in.nextInt();
    System.out.println("���������ڲ���������(1-50)��");
    int permonkeysnum = in.nextInt();
    System.out.println("�������������ٶ�(5-10)��");
    int MV = in.nextInt();
    
    // ������ɷ�����ٶ�
    int bornnum = 0;
    int borntime = 0;
    int ID = 1;
    Random random = new Random();
    while (bornnum < allmonkeysnum) {
      // ������ɷ���
      String direction;
      int direct = random.nextInt(2);
      if(direct == 0) {
        direction = "L->R";
      }else {
        direction = "R->L";
      }
      // ��������ٶ�
      int v = random.nextInt(MV-1)+1;
      
      // ���뵽datas��
      String[] data = new String[4];
      data[0] = String.valueOf(borntime);
      data[1] = String.valueOf(ID);
      data[2] = direction;
      data[3] = String.valueOf(v);
      datas.add(data);
      ID++;
      bornnum++;
      // ÿ������
      if((bornnum % permonkeysnum) == 0){
        borntime += circletime;
      }
    }
    
    // ��������
    generateLadders(laddersnum, rungsnum);
    
    // ��ʼ���ɺ���
    generateMonkeys();
  }
  
  /**
   * ���������ʺ͹�ƽ��.
   * @param simulator ģ��������.
   * @throws InterruptedException
   * @throws AWTException
   */
  private static void calculateTime(Simulator simulator) throws InterruptedException{
    // ��ʼ��ʱ
    // ��������
    int monkeysnum = monkeys.size();
    System.out.println("��ʼ���ӡ�");
    Simulator.writelog("��ʼ���ӡ�");
    long start = System.currentTimeMillis();
    simulator.startsimulator();
    while (!monkeys.isEmpty()) {
      print();
      System.out.println("&&������ӣ�" + monkeys.size());
      Thread.sleep(1000);
    }
    threadSignal.await();//�ȴ��������߳�ִ����
    long end = System.currentTimeMillis();
    System.out.println("������ɣ���ʱ��" + (end - start) + "ms");
    Simulator.writelog("������ɣ���ʱ��" + (end - start) + "ms");
    System.out.println("������Ϊ" + (double)monkeysnum*1000/(end - start));
    Simulator.writelog("������Ϊ" + (double)monkeysnum*1000/(end - start));
    calculatefairness();
    try {
      fw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("�ļ��ر�ʧ�ܣ�");
    }
    return;
  }
  
  /**
   * ���㹫ƽ��.
   */
  private static void calculatefairness() {
    int monkeynum = monkeyssequence.size();
    int sigma = 0;//  ��¼sigma���
    double denominator = monkeynum*(monkeynum-1)/(double)2;// ��ĸ
    for(int i = 0;i < monkeynum;i++) {
      for(int j = 0;j < monkeynum;j++) {
        if(i < j) {
          if(monkeyssequence.get(i).getBorntime() <= monkeyssequence.get(j).getBorntime()) {
            sigma ++;
          }else {
            sigma --;
          }
        } else if (i > j) {
          if(monkeyssequence.get(i).getBorntime() >= monkeyssequence.get(j).getBorntime()) {
            sigma ++;
          }else {
            sigma --;
          }
        }
      }
    }
    Simulator.writelog("��ƽ��Ϊ��" + (double)sigma/denominator);
    System.out.println("��ƽ��Ϊ��" + (double)sigma/denominator);
  }
  
  /**
   * ������.
   * @param args
   * @throws InterruptedException
   * @throws AWTException
   */
  public static void main(String[] args) throws InterruptedException {
    Simulator simulator = new Simulator();
    int choice = 0;// ѡ�����뷽ʽ
    while (true) {
      Simulator.ladders.clear();
      Simulator.monkeys.clear();
      simulator.datas.clear();
      simulator.subthreads.clear();
      simulator.count = 0;
      Simulator.monkeyssequence.clear();
      threadSignal = null;
      System.out.println("*******************");
      System.out.println("��ѡ���������뷽ʽ��");
      System.out.println("1:���ļ�");
      System.out.println("2:�ֶ�����");
      System.out.println("0:�˳�");
      choice = in.nextInt();
      
      switch (choice) {
        case 1:
          simulator.readData();
          calculateTime(simulator);
          break;
        case 2:
          simulator.inputData();
          calculateTime(simulator);
          break;
        case 0:
          in.close();
          System.exit(0);
          break;
         default:
           System.out.println("���벻�Ϸ������������롣");
           break;
      }
    }
  }

}
