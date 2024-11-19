# Contributing Guidelines

## How to contribute to kubernetes-webhooks-framework

We'd love to accept your patches! Since we **♥︎♥︎ LOVE ♥︎♥︎** Contributors and Contributions :-)

You can start contributing to this project by following the below guidelines:-

(We are assuming you know about git like resolving merge conflicts, squash, setting remote etc.)

### Getting Source Code

* Get the source code by doing a fork and then using the below command
```
git clone https://github.com/your_github_username/josdk-webhooks.git
```

* If you want to build/run the project, use command
```
mvn clean install
```

### Starting Development

Now you can start your contribution work.

#### * Finding the issue

There are lots of issues on kubernetes-webhooks-framework's [issue page](https://github.com/operator-framework/josdk-webhooks/issues). Please go through the issues and find a one which you want to fix/develop. If you want to implement something which is not there in the issues, please create a new issue. Please assign that new issue or already existing issue to yourself otherwise it may happen that someone else will fix the same issue.

#### Creating a new branch

Please create a new branch to start your development work. You can create the branch by any name but we will suggest you consider the naming convention like iss_issueNumber. Example - iss_989

```
git checkout -b iss_issueNumber
```

#### Create your PATCH

Do all your development or fixing work here.

#### Adding Unit and Regression Tests 

After all your development/fixing work is done, do not forget to add `Unit Test` and `Regression Test` around that. It will be nice if you can add an example of the new feature you have added.

#### Check your work after running all Unit and Regression Tests

You should run all the unit tests by hitting the following command

```shell
mvn clean install
```

#### Commit your work

After all your work is done, you need to commit the changes.
```
git commit -am "Commit-Message"
```
Please add a very elaborative [commit message](https://www.conventionalcommits.org/en/v1.0.0/) for the work you have done. It will help the reviewer to understand the things quickly.

#### Rebase the PR

It may happen that during the development, someone else submitted another PATCH that is merged before yours. You need to rebase your branch with current upstream master.

#### Build the project

Before sending the PR, check whether everything is working fine. To build the project and run test
```shell
mvn clean install
```

#### Format the files that you touched

```shell
mvn spotless:apply
```

#### Push the changes to your fork

```shell
git push origin iss_issueNumber
```

#### Create a Pull Request

Please create a Pull Request from GitHub to kubernetes-webhooks-framework:main. Do not forget to provide very brief Title and elaborative description of PR. Please link the PR to issue by adding `Fix #issueNumber` at the end of the description.

### PR Review

Your PR will get reviewed soon from the maintainers of the project. If they suggest changes, do all the changes, commit the changes, rebase the branch, squash the commits and push the changes. If all will be fine, your PR will be merged.

That's it! Thank you for your contribution!

### Note

Contribution can be very small, that does not matter. We even love to receive a typo fix PR. Adding feature or fixing a bug is not the only way to contribute. You can send us PR for adding documentation, fixing typos or adding tests.
