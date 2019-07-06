package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;

/**
 * ʹ�ô��ۺ�������ÿ�����ӵĴ��ۣ�����Խ��Խ��.��(n/d)*|selfv-frontv|��
 * @author ����
 *
 */
public class Costfunction implements ChoseBridge{
  @Override
  public Ladder chosenbridge(Monkey monkey, Map<Ladder, List<Map<Rung, Monkey>>> ladders) {
    synchronized (Rung.class) {
      List<Ladder> forwardladder = new ArrayList<Ladder>();// ���浱ǰ�������������
      Ladder lowercostladder = null;// ��¼�ٶ����Լ����������
      int h = 0;// ��¼̤����
      double cost = 200;
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
          // �������
          double thiscost = costfunction(monkey, rungs);
          if(thiscost < cost) {
            lowercostladder = l;
            cost = thiscost;
          }
        }
        return lowercostladder;
      }
      return null;
    }
  }
  
  private double costfunction(Monkey monkey, List<Map<Rung, Monkey>> rungs) {
    double frontv = 1;// ǰ������ٶ�
    int n = 0;// �������Ϻ�����
    int d = 1;// �������ϵ�һ�����Ӿ���
    boolean find = false;// �ҵ���һ������
    
    // L->R
    if(monkey.getDirection().equals("L->R")) {
      for(int i = 0;i < rungs.size();i++) {
        for(Rung rung:rungs.get(i).keySet()) {
          if(rungs.get(i).get(rung) != null) {
            n++;
            if(!find) {
              frontv = rungs.get(i).get(rung).getRealV();
              d = i;
              find = true;
            }
          }
        }
      }
    }
    
    // L->R
    if(monkey.getDirection().equals("L->R")){
      for(int i = rungs.size()-1;i >= 0;i--) {
        for(Rung rung:rungs.get(i).keySet()) {
          if(rungs.get(i).get(rung) != null) {
            n++;
            if(!find) {
              frontv = rungs.get(i).get(rung).getRealV();
              d = i;
              find = true;
            }
          }
        }
      }
    }
    
    // �������
    return n*(double)Math.abs(monkey.getV() - frontv)/d;
  }
}
