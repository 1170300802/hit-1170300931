package strategy;

import java.util.List;
import java.util.Map;
import ladder.Ladder;
import ladder.Rung;
import monkey.Monkey;

/**
 * ����ѡ��ӿ�.
 * @author ����
 *
 */
public interface ChoseBridge {
  /**
   * ���в���ѡ����.
   * @param monkey ��˼���ĺ���.
   * @param ladders ��������.
   * @return ѡ�е�����.
   */
  public Ladder chosenbridge(Monkey monkey, Map<Ladder, List<Map<Rung, Monkey>>> ladders);
}
