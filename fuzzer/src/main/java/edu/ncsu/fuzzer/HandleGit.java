package edu.ncsu.fuzzer;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class HandleGit {
	private Git repository = null;
	public HandleGit(Git repository) {
		this.repository = repository;
	}
	
	public void setGitRepository(Git repository) {
		this.repository = repository;
	}
	
	public void addFileToIndex(File fileToAdd, String gitLocalRepositoryPath) throws IOException, GitAPIException {
	    String filePath = fileToAdd.getAbsolutePath().replace("\\","/").substring(gitLocalRepositoryPath.length()+1);
	    repository.add().addFilepattern(filePath).call();
	}
	public void commitChanges(String commitMessage) throws GitAPIException {
	    CommitCommand commit = repository.commit();
	    commit.setAllowEmpty(false);
	    commit.setMessage(commitMessage).call();
	}
}
