package edu.ncsu.fuzzer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.errors.EmtpyCommitException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

public class HandleGit {
	private Git repository = null;
	private final static String BRANCHHEAD = "refs/heads/fuzzer";
	private final static String BRANCH = "fuzzer";
	private final static String authorName = "FuzzingTool";
	private final static String authorEmail = "fuzzingTool@ncsu.edu";
	private static Queue<RevCommit> commitQ = new LinkedList<>();

	public HandleGit(String repositoryURL) throws IOException, GitAPIException {

		Git repository = Git.open(new File(repositoryURL));
		this.repository = repository;
		boolean isPresent = false;
		// List the existing branches
		List<Ref> listRefsBranches = repository.branchList().setListMode(ListMode.ALL).call();

		for (Ref refBranch : listRefsBranches) {
			if (BRANCHHEAD.equals(refBranch.getName())) {
				isPresent = true;
				break;
			}
		}

		if (isPresent)
			checkoutBranch(BRANCH);
		else {
			createBranch(BRANCH);
			checkoutBranch(BRANCH);
		}

	}

	public void setGitRepository(Git repository) {
		this.repository = repository;
	}

	public void createBranch(String branchName) {
		// Create a new branch
		try {
			repository.branchCreate().setName(branchName).call();

		} catch (RefAlreadyExistsException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidRefNameException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void checkoutBranch(String branchName) {
		// Checkout the new branch
		try {
			repository.checkout().setName(branchName).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public void addFileToIndex() throws IOException, GitAPIException {
		repository.add().addFilepattern(".").call();
		repository.add().setUpdate(true).addFilepattern(".").call();
	}

	public void commitChanges(String commitMessage) throws GitAPIException, EmtpyCommitException {
		CommitCommand commit = repository.commit().setAuthor(authorName, authorEmail).setAllowEmpty(false);
		RevCommit rev = commit.setMessage(commitMessage).call();
		commitQ.add(rev);// Added to commitQ;
		System.out.println(commitQ);
	}

	public void revert() {
		RevertCommand revertCommand = repository.revert();
		try {
			revertCommand.include(commitQ.poll());
			commitQ.clear();
			RevCommit revCommit = revertCommand.call();
			System.out.println(revCommit);
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
}
