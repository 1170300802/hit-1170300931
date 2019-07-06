package strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;

/**
 * ����һ������ѡ��û�����ӵĺ��ӣ������������϶��к��ӣ���ѡ��û�ж���ĺ��ӣ����ж�������ѡ��
 * ��û�����������ģ��ȴ�.
 * @author ����
 *
 */
public class Strategyone implements ChoseBridge {
  @Override
  public Ladder chosenbridge(Monkey monkey, Map<Ladder, List<Map<Rung, Monkey>>> ladders) {
    synchronized (Rung.class) {
      List<Ladder> forwardladder = new ArrayList<Ladder>();// ���浱ǰ�������������
      boolean existforward = false;
      Random random = new Random();
      
      for(Ladder ladder:ladders.keySet()) {
        List<Map<Rung, Monkey>> rungs = ladders.get(ladder);
        // ��������ͬ��ֱ����
        if(Ladder.isEmpty(rungs)) {
          ladder.setdirection(monkey.getDirection());
          return ladder;
        }
      
        // ���Ƿ��з������
ladderloop:for(int i = 0;i < rungs.size();i++) {
          for(Rung rung:rungs.get(i).keySet()) {
            Monkey m = rungs.get(i).get(rung);
            if(m != null) {
              // ����ֱ������һ������
              if(!m.getDirection().equals(monkey.getDirection())) {
                break ladderloop;
              }
              else {// �ȼ�¼����ģ����û�пյ���������·
                forwardladder.add(ladder);
                existforward = true;
              }
            }
          }
        }
    }
      if(existforward) {
        return forwardladder.get(random.nextInt(forwardladder.size()));
      }
      return null;
    }
  }
}
