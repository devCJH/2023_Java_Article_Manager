package com.KoreaIT.java.AM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.AM.dto.Article;
import com.KoreaIT.java.AM.dto.Member;
import com.KoreaIT.java.AM.util.Util;

public class App {
	private List<Article> articles;
	private List<Member> members;

	public App() {
		articles = new ArrayList<>();
		members = new ArrayList<>();
	}

	public void run() {
		System.out.println("== 프로그램 시작 ==");
		Scanner sc = new Scanner(System.in);

		makeTestData();

		int lastArticleId = 3;
		int lastMemberId = 0;

		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();

			if (cmd.length() == 0) {
				System.out.println("명령어를 입력해주세요");
				continue;
			}

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("member join")) {

				int id = lastMemberId + 1;
				lastMemberId = id;
				String regDate = Util.getNowDateStr();
				
				String loginId = null;
				
				while(true) {
					System.out.printf("로그인 아이디 : ");
					loginId = sc.nextLine();
					
					if (isLoginIdDup(loginId) == false) {
						System.out.printf("%s은(는) 이미 사용중인 아이디 입니다\n", loginId);
						continue;
					}
					
					System.out.printf("%s은(는) 사용가능한 아이디 입니다\n", loginId);
					break;
				}
				
				String loginPw = null;
				
				while(true) {
					System.out.printf("로그인 비밀번호 : ");
					loginPw = sc.nextLine();
					System.out.printf("로그인 비밀번호 확인 : ");
					String loginPwChk = sc.nextLine();
					
					if (loginPw.equals(loginPwChk) == false) {
						System.out.println("비밀번호를 다시 입력해주세요");
						continue;
					}
					break;
				}
				
				System.out.printf("이름 : ");
				String name = sc.nextLine();

				Member member = new Member(id, regDate, loginId, loginPw, name);

				members.add(member);

				System.out.printf("%s 회원님 환영합니다\n", name);

			} else if (cmd.equals("article write")) {

				int id = lastArticleId + 1;
				lastArticleId = id;
				String regDate = Util.getNowDateStr();
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();

				Article article = new Article(id, regDate, title, body);

				articles.add(article);

				System.out.printf("%d번글이 생성되었습니다\n", id);

			} else if (cmd.startsWith("article list")) {

				if (articles.size() == 0) {
					System.out.println("게시글이 없습니다");
					continue;
				}
				
				String searchKeyword = cmd.substring("article list".length()).trim();
				
				List<Article> forPrintArticles = articles;
				
				if (searchKeyword.length() > 0) {
					forPrintArticles = new ArrayList<>();
					
					for (Article article : articles) {
						if (article.title.contains(searchKeyword)) {
							forPrintArticles.add(article);
						}
					}
					
					if (forPrintArticles.size() == 0) {
						System.out.println("검색결과가 없습니다");
						continue;
					}
				}

				System.out.println("번호	|	제목	|		날짜		|	조회수	");
				
				for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
					Article article = forPrintArticles.get(i);
					System.out.printf("%d	|	%s	|	%s	|	%d	\n", article.id, article.title, article.regDate,
							article.hit);
				}

			} else if (cmd.startsWith("article detail ")) {

				String[] cmdBits = cmd.split(" ");
				int id = Integer.parseInt(cmdBits[2]);

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}

				foundArticle.increaseHit();

				System.out.println("== 게시글 상세보기 ==");
				System.out.printf("번호 : %d\n", foundArticle.id);
				System.out.printf("날짜 : %s\n", foundArticle.regDate);
				System.out.printf("제목 : %s\n", foundArticle.title);
				System.out.printf("내용 : %s\n", foundArticle.body);
				System.out.printf("조회수 : %s\n", foundArticle.hit);

			} else if (cmd.startsWith("article modify ")) {

				String[] cmdBits = cmd.split(" ");
				int id = Integer.parseInt(cmdBits[2]);

				Article foundArticle = getArticleById(id);

				if (foundArticle == null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}

				System.out.printf("수정할 제목 : ");
				String title = sc.nextLine();
				System.out.printf("수정할 내용 : ");
				String body = sc.nextLine();

				foundArticle.title = title;
				foundArticle.body = body;

				System.out.printf("%d번 게시글이 수정되었습니다\n", id);

			} else if (cmd.startsWith("article delete ")) {

				String[] cmdBits = cmd.split(" ");
				int id = Integer.parseInt(cmdBits[2]);

				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
					continue;
				}

				articles.remove(foundArticle);

				System.out.printf("%d번 게시글이 삭제되었습니다\n", id);

			} else {
				System.out.printf("%s(은)는 존재하지 않는 명령어입니다\n", cmd);
			}

		}

		sc.close();

		System.out.println("== 프로그램 끝 ==");
	}

	private void makeTestData() {
		System.out.println("테스트를 위한 데이터를 생성합니다");

		articles.add(new Article(1, Util.getNowDateStr(), "test1", "test1", 10));
		articles.add(new Article(2, Util.getNowDateStr(), "test2", "test2", 15));
		articles.add(new Article(3, Util.getNowDateStr(), "test3", "test3", 28));
	}
	
	private Article getArticleById(int id) {
		for (int i = 0; i < articles.size(); i++) {
			Article article = articles.get(i);

			if (article.id == id) {
				return article;
			}
		}
		return null;
	}
	
	private boolean isLoginIdDup(String loginId) {
		for (Member member : members) {
			if (member.loginId.equals(loginId)) {
				return false;
			}
		}
		return true;
	}
}
