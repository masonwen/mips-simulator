public class TestMemory {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		// ʹ��getInstance������ȡ��������Ψһʵ��
		Memory m = Memory.getInstance();
		Keyboard k = Keyboard.getInstance();
		Display d = Display.getInstance();
	
		// �����ڴ�
		m.writeString(Memory.TEXTADDR, "This is some text\0");
		System.out.println(m.readString(Memory.TEXTADDR));
		
		m.write(1000, -1234);
		System.out.println(m.read(1000));
		
		// �����ı�ģʽ��ʾ
		m.write(Memory.DISPMODE, Memory.TEXTMODE);		
		m.writeTextMode(10, 6, 'c');
		
		// ����ͼ��ģʽ��ʾ
		//m.write(Memory.DISPMODE, Memory.GRAPHICMODE);
		for(int x = 10; x < 100; x ++)
			m.writeGraphicMode(x, x, 1, 0, 1);
	}

}
