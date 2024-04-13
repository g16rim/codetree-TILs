import java.io.*;
import java.util.*;

public class Main {
	static int n, m, k, map[][], sx, sy, l, cnt;
	static int r[], c[], dist[]; // 0: 출구 좌표, 1 ~ m: 참가자 좌표
	static int dr[] = { -1, 1, 0, 0 }, dc[] = { 0, 0, -1, 1 };

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		map = new int[n + 2][n + 2];
		for (int i = 1; i <= n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		r = new int[m + 1];
		c = new int[m + 1];
		dist = new int[m + 1]; // 참가자들의 이동 거리 합
		for (int i = 1; i <= m; i++) {
			st = new StringTokenizer(br.readLine());
			r[i] = Integer.parseInt(st.nextToken());
			c[i] = Integer.parseInt(st.nextToken());
		}
		st = new StringTokenizer(br.readLine());
		r[0] = Integer.parseInt(st.nextToken());
		c[0] = Integer.parseInt(st.nextToken());

		boolean allArrived = false;
		for (int t = 1; t <= k; t++) {
			// 참가자 이동
			for (int i = 1; i <= m; i++) {
				movePiece(i);
			}
			// 미로 회전
			rotateMap();
			// 모두 도착했으면
			if (cnt == m) break;
//			printMap();
//			printPos();
//			System.out.println(t + "초 이동 " + Arrays.toString(dist));
		}
		StringBuilder sb = new StringBuilder();
		int result = 0;
		for (int i = 1; i <= m; i++)
			result += dist[i];
		sb.append(result).append("\n");
		sb.append(r[0]).append(" ").append(c[0]);
		System.out.println(sb);
	}

	static void rotateMap() {
		l = n; // 정사각형 길이
		// 완탐
		for (int i = 1; i <= m; i++) {
			if (r[i] == 0 && c[i] == 0) continue;
			int dist = Math.max(Math.abs(r[i] - r[0]), Math.abs(c[i] - c[0])) + 1;
			boolean next = false;
			if (dist <= l) {
				// 정사각형이 만들어질 수 있는지 판단
				for (int x = 1; x <= n - dist + 1; x++) { // 정사각형 시작 좌표
					for (int y = 1; y <= n - dist + 1; y++) {
						if (r[i] >= x && r[i] < x + dist && c[i] >= y && c[i] < y + dist) { // 참가자
							if (r[0] >= x && r[0] < x + dist && c[0] >= y && c[0] < y + dist) { // 출구
								// 찾았다. 다음 사람 넘어가기
								if (dist < l) {
									l = dist;
									sx = x;
									sy = y;
								} else {
									if (sx >= x) {
										sx = x;
										if (sy > y) {
											sy = y;
										}
									}
								}
								next = true;
								break;
							}
						}
					}
					if (next) {
						break;
					}
				}
			}
		}

		// sx ~ sx + l, sy + sy + l
		int temp[][] = copyArr();
		// 시계방향 90도 회전, 그 중 벽은 내구도 1씩 깎기
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < l; j++) {
				if (temp[i][j] > 0)
					map[sx + i][sy + j] = temp[i][j] - 1;
				else
					map[sx + i][sy + j] = temp[i][j];
			}
		}
		// 참가자 + 출구 이동
		for (int i = 0; i <= m; i++) {
			if (r[i] >= sx && r[i] < sx + l && c[i] >= sy && c[i] < sy + l) {
				int tr = r[i] - sx;
				int tc = c[i] - sy;
				int t = tr;
				tr = tc;
				tc = l - t - 1;
				r[i] = tr + sx;
				c[i] = tc + sy;
			}
		}
	}
	
	static int[][] copyArr() {
//		System.out.println("copyArr: " + l + " " + sx + " " + sy);
		int[][] temp = new int[l][l];
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < l; j++) {
				temp[i][j] = map[sx + i][sy + j];
			}
		}
		for (int i = 0; i < l / 2; i++) {
			for (int j = 0; j < l; j++) {
				int t = temp[i][j];
				temp[i][j] = temp[l - i - 1][j];
				temp[l - i - 1][j] = t;
			}
		}
		for (int i = 0; i < l; i++) {
			for (int j = i; j < l; j++) {
				int t = temp[i][j];
				temp[i][j] = temp[j][i];
				temp[j][i] = t;
			}
		}
		return temp;
	}

	static int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	static void movePiece(int idx) {
		int d = getDistance(r[idx], c[idx], r[0], c[0]);
		boolean moved = false;

		int dir = 0;
		for (int i = 0; i < 4; i++) {
			int nr = r[idx] + dr[i];
			int nc = c[idx] + dc[i];

			// 범위 밖 or 벽
			if (nr < 1 || nc < 1 || nr > n || nc > n || map[nr][nc] > 0)
				continue;

			if (getDistance(nr, nc, r[0], c[0]) == 0) { // 출구 도착 
				r[idx] = 0;
				c[idx] = 0;
				dist[idx] += 1;
				cnt++;
				return;
			} else if (getDistance(nr, nc, r[0], c[0]) < d) {
//				System.out.println("변화: " + idx + "번째 " + nr + " " + nc);
//				printPos();
				d = getDistance(nr, nc, r[0], c[0]);
				moved = true;
				dir = i;
			}
		}

		if (moved) {
			dist[idx] += 1;
			r[idx] += dr[dir];
			c[idx] += dc[dir];
		}
	}

	static void printPos() {
		for (int i = 0; i <= m; i++) {
			System.out.println(i + " " + r[i] + " " + c[i]);
		}
	}

	static void printMap() {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
	}
}