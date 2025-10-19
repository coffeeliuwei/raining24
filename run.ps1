$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$src = Join-Path $root 'src'
$bin = Join-Path $root 'bin'
New-Item -ItemType Directory -Force -Path $bin | Out-Null
# 收集所有Java源文件
$files = Get-ChildItem -Path $src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if ($files.Count -eq 0) { Write-Error 'No Java files found under src/' }
# 编译
javac -encoding UTF-8 -d $bin $files
# 运行
java -cp $bin com.training.Main