import java.io.*;
import java.util.*;

public class Main {
	static int l, n, q, map[][], r[], c[], h[], w[], k[], origK[], nr[], nc[], dmg[];
	static int dr[] = { -1, 0, 1, 0 }, dc[] = { 0, 1, 0, -1 };

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		l = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());

		map = new int[l + 1][l + 1];
		for (int i = 1; i <= l; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= l; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		r = new int[n + 1];
		c = new int[n + 1];
		h = new int[n + 1];
		w = new int[n + 1];
		k = new int[n + 1];
		origK = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			st = new StringTokenizer(br.readLine());
			r[i] = Integer.parseInt(st.nextToken());
			c[i] = Integer.parseInt(st.nextToken());
			h[i] = Integer.parseInt(st.nextToken());
			w[i] = Integer.parseInt(st.nextToken());
			k[i] = Integer.parseInt(st.nextToken());
			origK[i] = k[i];
		}
		
		nr = new int[n + 1];
		nc = new int[n + 1];
		dmg = new int[n + 1];
		for (int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine());
			movePiece(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		}
		
		// 결과 계산, 출력
		long result = 0;
		for (int i = 1; i <= n; i++) {
			if (k[i] > 0)
				result += origK[i] - k[i];
		}
		
		System.out.println(result);
	}
	
	static void movePiece(int idx, int dir) {
		// 체력 이미 끝났으면 끝내기
		if (k[idx] <= 0) return;
		
		if (canMove(idx, dir)) {
			for (int i = 1; i <= n; i++) {
				// 새로운 위치, 데미지 체크
				r[i] = nr[i];
				c[i] = nc[i];
				k[i] -= dmg[i];
			}
		}
	}
	
	static boolean canMove(int idx, int dir) {
		Queue<Integer> q = new ArrayDeque<>();
		boolean[] isMoved = new boolean[n + 1];
		
		for (int i = 1; i <= n; i++) {
			dmg[i] = 0;
			nr[i] = r[i];
			nc[i] = c[i];
			isMoved[i] = false;
		}
		
		q.add(idx);
		isMoved[idx] = true;
		
		while (!q.isEmpty()) {
			int x = q.poll();
			
			nr[x] += dr[dir];
			nc[x] += dc[dir];
			
			if (nr[x] < 1 || nc[x] < 1 || nr[x] + h[x] - 1 > l || nc[x] + w[x] - 1 > l)
				return false;
			
			for (int i = nr[x]; i <= nr[x] + h[x] - 1; i++) {
				for (int j = nc[x]; j <= nc[x] + w[x] - 1; j++) {
					if (map[i][j] == 1)
						dmg[x]++;
					if (map[i][j] == 2)
						return false;
				}
			}
			
			for (int i = 1; i <= n; i++) {
				if (isMoved[i] || k[i] <= 0)
					continue;
				if (r[i] > nr[x] + h[x] - 1 || nr[x] > r[i] + h[i] - 1)
					continue;
				if (c[i] > nc[x] + w[x] - 1 || nc[x] > c[i] + w[i] - 1)
					continue;
				
				isMoved[i] = true;
				q.add(i);
			}
		}
		
		dmg[idx] = 0;
		return true;
	}
}