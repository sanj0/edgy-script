using System;
using System.Diagnostics;
using System.Text;

namespace esrun
{
    class ESRun
    {

        private static readonly string e80BinPath = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile) + "\\.edgy-script\bin\\e80.jar";
        public static void Main(string[] args)
        {
            if (args.Length != 0) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < args.Length; i++)
                {
                    builder.Append(args[i]);
                }
            } else
            {
                Run80("");
            }
        }

        private static void Run80(string args)
        {
            Process process = new Process();
            process.EnableRaisingEvents = false;
            process.StartInfo.FileName = "java";
            process.StartInfo.Arguments = "-cp " + e80BinPath + " de.edgelord.edgyscript.main.Main " +  args; 
            process.Start();
        }
    }
}
