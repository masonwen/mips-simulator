public class Test {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		// ʹ��getInstance������ȡ��������Ψһʵ��
		Memory m = Memory.getInstance();
		Keyboard k = Keyboard.getInstance();
		Display d = Display.getInstance();
		
		Cpu c = new Cpu();
		
		System.out.println("=w=");
	/*
		// �����ڴ�,�����ı�ģʽ��ʾ
		m.write(Memory.DISPMODE, Memory.TEXTMODE);
		
		m.writeString(Memory.TEXTADDR, "This is some text.\0");
		System.out.println(m.readString(Memory.TEXTADDR));
	
		m.writeTextMode(10, 6, 'c');
		
		// ����ͼ��ģʽ��ʾ
		//m.write(Memory.DISPMODE, Memory.GRAPHICMODE);
		for(int t = 0; t < 100; t += 10)
			for(int x = 10; x < 100; x ++)
				m.writeGraphicMode(x, x+t, 1, 0, 1);
		*/
	}

}
