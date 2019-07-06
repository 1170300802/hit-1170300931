package ladder;

import java.util.List;
import java.util.Map;
import monkey.Monkey;

/**
 * �������ӵ����ݽṹ.
 * @author ����
 *
 */
public class Ladder {
  private int ID = 0;// ID���
  private String direction = "";// ��ʼ�������ֹ����
  
  // RI:
  //    ID��ʾ�����ӵı�ţ���Ψһ��.
  // AF:
  //    ID�Ƕ�ÿ�����ӵı��
  // safety from rep exposure:
  //    ID�ǲ��ɱ����ͣ�Ψһ�ķ��ط���ʹ���˷������л�ָ��.
  
  /**
   * ������һ�����Ӳ�������.
   * @param ID ���ӵı��.
   */
  public Ladder(int ID, String direction) {
    this.ID = ID;
    this.direction = direction;
  }

  /**
   * ����ID���.
   * @return ID ���ӱ�ţ���һ����Ȼ��.
   */
  public int getID() {
    int copy = 0;
    copy = this.ID;
    return copy;
  }

  /**
   * �������ӳ�ʼ����.
   * @return
   */
  public String getdirection() {
    return direction;
  }
  
  /**
   * ���÷���.
   * @param direction ���÷���
   * @return
   */
  public void setdirection(String direction) {
    this.direction = direction;
  }
  
  /**
   * �ж�̤���Ƿ�Ϊ��.
   * @param rungs �������ϵ�����̤��.
   * @return ������򷵻�true�����򷵻�false.
   */
  public synchronized static boolean isEmpty(List<Map<Rung, Monkey>> rungs) {
    for(Map<Rung, Monkey> rung:rungs) {
      for(Rung r:rung.keySet()) {
        if(rung.get(r) != null) {
          return false;
        }
      }
    }
    return true;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ID;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Ladder other = (Ladder) obj;
    if (ID != other.ID)
      return false;
    return true;
  }
}
