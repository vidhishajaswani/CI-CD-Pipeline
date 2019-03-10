package edu.ncsu.fuzzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class ItrustFuzzing {
	private HandleGit git;
	private List<String> paths = new ArrayList<>();
	private Random random = new Random();

	public ItrustFuzzing(String repoURL, HandleGit git) {
		this.git = git;
		try {
			// pass the path to the file as a parameter
			File file = new File("itrust_source_files.txt");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine())
				paths.add(repoURL + sc.nextLine());
			sc.close();
			// System.out.println(paths);

			doFuzzing();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void doFuzzing() throws IOException {
		for (String path : paths) {
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
				if (file.isFile()) {
					// System.out.println(file.getAbsolutePath());
					CompilationUnit compilationUnit = StaticJavaParser.parse(file);
					System.out.println(compilationUnit.toString());
					changeStringConstants(compilationUnit);
					swap0_1(compilationUnit);
					swapRelatationOperator(compilationUnit);
					swapAssignmentOperator(compilationUnit);
					swapEqualsOperator(compilationUnit);
					System.out.println(compilationUnit.toString());
					System.out.println(
							"-----------------------------------------------------------------------------------------------------");
					FileWriter wr = new FileWriter(file);
					wr.write(compilationUnit.toString());
					wr.close();
				}
			}

		}
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
		compilationUnit
				.walk(BinaryExpr.class,
						e -> e.setOperator((e.getOperator() == BinaryExpr.Operator.GREATER_EQUALS)
								? BinaryExpr.Operator.LESS_EQUALS
								: BinaryExpr.Operator.GREATER_EQUALS));
	}

	// This function swap = & +=
	public void swapAssignmentOperator(CompilationUnit compilationUnit) {
		compilationUnit.walk(AssignExpr.class,
				e -> e.setOperator((e.getOperator() == Operator.ASSIGN) ? Operator.PLUS : Operator.ASSIGN));
	}

	// This function swap == & !=
	public void swapEqualsOperator(CompilationUnit compilationUnit) {
		compilationUnit.walk(BinaryExpr.class,
				e -> e.setOperator((e.getOperator() == BinaryExpr.Operator.EQUALS) ? BinaryExpr.Operator.NOT_EQUALS
						: BinaryExpr.Operator.EQUALS));
	}

}
