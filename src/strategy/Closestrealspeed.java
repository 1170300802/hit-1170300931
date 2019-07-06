package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;

/**
 * ���Զ���ѡ��������ӵ��ٶ����Լ����������
 * @author ����
 *
 */
public class Closestrealspeed implements ChoseBridge {

  @Override
  public Ladder chosenbridge(Monkey monkey, Map<Ladder, List<Map<Rung, Monkey>>> ladders) {
    synchronized (Rung.class) {
      List<Ladder> forwardladder = new ArrayList<Ladder>();// ���浱ǰ�������������
      Ladder closestladder = null;// ��¼�ٶ����Լ����������
      int h = 0;// ��¼̤����
      int closestspeedmonkey = 10;
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
                  if(Math.abs(m.getRealV() - monkey.getV()) < closestspeedmonkey) { // �ٶȸ���
                    closestspeedmonkey = Math.abs(m.getRealV() - monkey.getV());
                    closestladder = l;
                    break ladderloop2;
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
                  if(Math.abs(m.getRealV() - monkey.getV()) < closestspeedmonkey) { // �ٶȸ���
                    closestspeedmonkey = Math.abs(m.getRealV() - monkey.getV());
                    closestladder = l;
                    break ladderloop3;
                    }
                  }
                }
              }
            }
          }
        return closestladder;
      }
      return null;
    }
  }
}
