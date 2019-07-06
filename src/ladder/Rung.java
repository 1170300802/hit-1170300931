package ladder;

/**
 * ���������ϵ�̤�����ݽṹ.
 * @author ����
 *
 */
public class Rung {
  // ̤����
  private int ID = 0;  
  // RI:
  //    ID��һ����Ȼ��
  // AF:
  //    ID��ʾ��̤��ı�ţ�һ�������ϲ�̤ͬ���Ų�ͬ.
  // safety from rep exposure:
  //    û���κη��ط���
  
  /**
   * �����Ŵ���һ���µ�̤��.
   * @param ID ̤���ţ���һ����Ȼ��.
   */
  public Rung(int ID) {
    this.ID = ID;
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
    Rung other = (Rung) obj;
    if (ID != other.ID)
      return false;
    return true;
  }
}
