const assert = require('node:assert/strict');
const path = require('node:path');
const fs = require('node:fs/promises');

async function main() {
  const root = path.resolve(__dirname, '..');
  const garden = path.resolve(process.argv[2] || path.join(root, '..', 'hackgarden2026-main', 'garden'));
  const { readProject } = require(path.join(garden, 'io.cjs'));
  const { execute } = require(path.join(garden, 'workflow.cjs'));
  const { validateCatalog } = require(path.join(garden, 'scanners.cjs'));
  const graph = JSON.parse(await fs.readFile(path.join(root, 'workflow.json'), 'utf8'));
  const catalog = validateCatalog(JSON.parse(await fs.readFile(path.join(root, 'advisories.json'), 'utf8')));
  const report = await execute(graph, await readProject(root), catalog, {});
  assert.equal(report.ok, true);
  const counts = Object.fromEntries([...new Set(report.findings.map(f => f.rule))].sort().map(rule => [rule, report.findings.filter(f => f.rule === rule).length]));
  assert.deepEqual(counts, {
    'sca.range': 1, 'secret.literal': 2, 'secret.private-key': 1,
    'spring.csrf': 1, 'spring.public': 1, 'sql.parameter': 1
  });
  assert.equal(report.findings.length, 7);
  assert.equal(report.findings.filter(f => f.severity === 'critical').length, 1);
  for (const f of report.findings) console.log(`${f.severity}: ${f.rule} ${f.file}:${f.line}`);
  console.log('OK: Cyber Garden detected all 7 intentional findings.');
}
main().catch(error => { console.error(error); process.exitCode = 1; });
