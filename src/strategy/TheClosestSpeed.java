package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;

/**
 * ����ٶȲ��ԣ�
 * 1:�п�������ѡ������.
 * 2:û�п����������޶����н����ӵ�������ѡ:
 *    a:ѡ�����������ӣ����Լ�����ĺ��ӵ��ٶ��Դ����Լ�
 *    b:���������Ӳ����ڣ���ѡ�����Լ�����ĺ��ӵ��ٶ���������
 * 3:���������ڣ���ȴ�
 * @author ����
 *
 */
public class TheClosestSpeed implements ChoseBridge{
  
  @Override
  public Ladder chosenbridge(Monkey monkey, Map<Ladder, List<Map<Rung, Monkey>>> ladders) {
    synchronized (Rung.class) {
      List<Ladder> forwardladder = new ArrayList<Ladder>();// ���浱ǰ�������������
      Ladder slowladder = null;// ��¼���Լ���������
      Ladder fastladder = null;// ��¼���Լ��������
      int h = 0;// ��¼̤����
      int slowmonkey = 0;
      int fastmonkey = 10;
      boolean existforward = false;
      
      // ��������
      for(Ladder ladder:ladders.keySet()) {
        List<Map<Rung, Monkey>> rungs = ladders.get(ladder);
        h = rungs.size();
        // ��������ͬ��ֱ����
        if(Ladder.isEmpty(rungs)) {
          ladder.setdirection(monkey.getDirection());
          return ladder;
        }
      
        // ���Ƿ��з������
ladderloop1: for(int i = 0;i < rungs.size();i++) {
          for(Rung rung:rungs.get(i).keySet()) {
            Monkey m = rungs.get(i).get(rung);
            if(m != null) {
              // ����ֱ������һ������
              if(!m.getDirection().equals(monkey.getDirection())) {
                break ladderloop1;
              }
              else {// �ȼ�¼����ģ����û�пյ���������·
                forwardladder.add(ladder);
                existforward = true;
                break ladderloop1;
              }
            }
          }
        }
      }
      if(existforward) {
        // ��ȡ�����������ӵ��ٶ�
        for(Ladder l:forwardladder) {
          List<Map<Rung, Monkey>> rungs = ladders.get(l);
          // L->R��������ɨ
          if(monkey.getDirection().equals("L->R")) {
ladderloop2:for(int i = 0;i < rungs.size();i++) {
              for(Rung rung:rungs.get(i).keySet()) {
                Monkey m = rungs.get(i).get(rung);
                if(m != null) { // �������Լ�����ĺ���
                  if(m.getV() < monkey.getV()) { // ���Լ��ٶ���
                    if(m.getV() > slowmonkey) { // ���ú��������п�ģ��������
                      slowmonkey = m.getV();
                      slowladder = l;
                      break ladderloop2;
                    }
                  }
                  else { // �������Լ�
                    if(m.getV() < fastmonkey) { // ���ú����ǿ������ģ��������
                      fastmonkey = m.getV();
                      fastladder = l;
                      break ladderloop2;
                    }
                  }
                }
              }
            }
          }
          
          // R-L��������ɨ
          if(monkey.getDirection().equals("R->L")) {
ladderloop3:for(int i = h-1;i >= 0;i--) {
              for(Rung rung:rungs.get(i).keySet()) {
                Monkey m = rungs.get(i).get(rung);
                if(m != null) { // �������Լ�����ĺ���
                  if(m.getV() < monkey.getV()) { // ���Լ��ٶ���
                    if(m.getV() > slowmonkey) { // ���ú��������п�ģ��������
                      slowmonkey = m.getV();
                      slowladder = l;
                      break ladderloop3;
                    }
                  }
                  else { // �������Լ�
                    if(m.getV() < fastmonkey) { // ���ú����ǿ������ģ��������
                      fastmonkey = m.getV();
                      fastladder = l;
                      break ladderloop3;
                    }
                  }
                }
              }
            }
          }
          // ���ڱ��Լ��ٶȴ�ĺ���
          if(fastladder != null) {
            fastladder.setdirection(monkey.getDirection());
            return fastladder;
          }
          slowladder.setdirection(monkey.getDirection());
          return slowladder;
        }
      }
      return null;
    }
  }
}
