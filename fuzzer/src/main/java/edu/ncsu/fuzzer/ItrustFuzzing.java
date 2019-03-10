package edu.ncsu.fuzzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class ItrustFuzzing {
	private HandleGit git;
	private List<String> paths = new ArrayList<>();
	private Random random = new Random();

	public ItrustFuzzing(String repoURL, HandleGit git) throws GitAPIException {
		this.git = git;
		try {
			// pass the path to the file as a parameter
			File file = new File("itrust_source_files.txt");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine())
				paths.add(repoURL + sc.nextLine());
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doFuzzing() throws IOException, GitAPIException {
		for (int i = 1; i <= 100; i++) {
			for (String path : paths) {
				File folder = new File(path);
				File[] listOfFiles = folder.listFiles();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						// System.out.println(file.getAbsolutePath());
						CompilationUnit compilationUnit = StaticJavaParser.parse(file);
						// System.out.println(compilationUnit.toString());
						int rand = random.nextInt(10);
						// introducing randomness
						if (rand <= 4) // probability 50%
							changeStringConstants(compilationUnit);
						if (rand >= 2 && rand <= 6) // probability 50%
							swap0_1(compilationUnit);
						if (rand >= 3 && rand <= 9) // probability 70%
							swapRelatationOperator(compilationUnit);
						if (rand >= 5 && rand <= 7) // probability 30%
							swapAssignmentOperator(compilationUnit);
						if (rand >= 7 && rand < 10) // probability 30%
							swapEqualsOperator(compilationUnit);

						FileWriter wr = new FileWriter(file);
						wr.write(compilationUnit.toString());
						wr.close();
					}
				}
			}
			// commit all changes!
			git.addFileToIndex();
			git.commitChanges("Fuzzing commit " + i);
		}
		git.revert(); // reverting back to original commit
		System.out.println("Fuzzing Completed!");
	}

	// This function changes string constant
	public void changeStringConstants(CompilationUnit compilationUnit) {
		compilationUnit.walk(StringLiteralExpr.class, e -> e.setString("FUZZY"));
	}

	// This function swap true & false
	public void swap0_1(CompilationUnit compilationUnit) {
		compilationUnit.walk(BooleanLiteralExpr.class, e -> e.setValue(e.getValue() ? false : true));
	}

	// This function swap <= & >=
	public void swapRelatationOperator(CompilationUnit compilationUnit) {
		compilationUnit.walk(BinaryExpr.class, e -> e.setOperator(getCorrect(e)));
	}

	// This function swap = & +=
	public void swapAssignmentOperator(CompilationUnit compilationUnit) {
		compilationUnit.walk(AssignExpr.class, e -> e.setOperator(getCorrect(e)));
	}

	// This function swap == & !=
	public void swapEqualsOperator(CompilationUnit compilationUnit) {
		compilationUnit.walk(BinaryExpr.class, e -> e.setOperator(getCorrect(e)));
	}

	private BinaryExpr.Operator getCorrect(BinaryExpr e) {
		if (e.getOperator() == BinaryExpr.Operator.EQUALS)
			return BinaryExpr.Operator.NOT_EQUALS;
		if (e.getOperator() == BinaryExpr.Operator.NOT_EQUALS)
			return BinaryExpr.Operator.EQUALS;
		if (e.getOperator() == BinaryExpr.Operator.GREATER_EQUALS)
			return BinaryExpr.Operator.LESS_EQUALS;
		if (e.getOperator() == BinaryExpr.Operator.LESS_EQUALS)
			return BinaryExpr.Operator.GREATER_EQUALS;

		return e.getOperator();
	}

	private AssignExpr.Operator getCorrect(AssignExpr e) {
		if (e.getOperator() == AssignExpr.Operator.ASSIGN)
			return AssignExpr.Operator.PLUS;
		if (e.getOperator() == AssignExpr.Operator.PLUS)
			return AssignExpr.Operator.ASSIGN;

		return e.getOperator();
	}

}
