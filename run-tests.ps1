$ErrorActionPreference = "Stop"

$junit = "lib/junit-platform-console-standalone-1.11.3.jar"

Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out/main, out/test | Out-Null

$mainSources = Get-ChildItem -Recurse `
    src/br/sistema/bancario/model, `
    src/br/sistema/bancario/repository, `
    src/br/sistema/bancario/service `
    -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out/main $mainSources
$testSources = Get-ChildItem -Recurse test -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "out/main;$junit" -d out/test $testSources
java -jar $junit execute -cp "out/main;out/test" --scan-classpath --details=tree